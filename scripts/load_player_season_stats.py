from os import getenv
from pathlib import Path

# Load variables from the .env file
from dotenv import load_dotenv

# league-wide player season stats endpoint -> returns many players in one request
from nba_api.stats.endpoints import leaguedashplayerstats

# PostgreSQL driver for Python
import psycopg2

# -------------------------
# STEP 0: LOAD ENVIRONMENT VARIABLES
# -------------------------

# Load values from .env so we can read DB credentials safely
env_path = Path(__file__).resolve().parent.parent / ".env"
load_dotenv(dotenv_path=env_path, override=True)

# Read database configuration from environment variables
db_host = getenv("DB_HOST")
db_port = getenv("DB_PORT", "5432")  # Use 5432 by default if DB_PORT isn't set
db_name = getenv("DB_NAME")
db_user = getenv("DB_USER")
db_password = getenv("DB_PASSWORD")

print(f"Using env file: {env_path}")
print(f"db_host: {db_host}")
print(f"db_user: {db_user}")
# Fail early if required environment variables are missing
if not db_host or not db_name or not db_user or not db_password:
    raise ValueError("Missing one or more required database environment variables.")

# -------------------------
# STEP 1: CHOOSE THE SEASON WE WANT TO LOAD
# -------------------------

# Using 2024-2025 season because the stats are already complete
season_to_load = "2024-25"


# -------------------------
# STEP 2: PREP VARIABLES
# -------------------------

# Start with None so these names exist even if connection setup fails
connection = None
cursor = None

# Track what occurred after script runs
inserted_or_updated_count = 0
skipped_count = 0

try:
    # -------------------------
    # STEP 3: CONNECT TO POSTGRES
    # -------------------------
    connection = psycopg2.connect(
        host=db_host,
        port=db_port,
        dbname=db_name,
        user=db_user,
        password=db_password,
    )

    # Open a cursor so we can execute SQL
    cursor = connection.cursor()

    # -------------------------
    # STEP 4: BUILD PLAYER ID LOOKUP
    # -------------------------

    # Translate NBA player IDs from nba_api into our database's internal player IDs.
    # Ex:
    # players.nba_player_id = 203518 -> players.id = 10
    # Load this mapping into a dictionary to achieve fast lookup
    cursor.execute(
        """
        SELECT id, nba_player_id
        FROM players
        """
    )

    # grab all the rows
    player_lookup_rows = cursor.fetchall()

    # use a dictionary for fast lookup
    nba_player_id_to_db_player_id = {}

    # build the dictionary
    for db_player_id, nba_player_id in player_lookup_rows:
        # translate the player id key -> value is our db player_id
        nba_player_id_to_db_player_id[nba_player_id] = db_player_id

    # print statement to check that we successfully mapped the ids
    print(f"Loaded {len(nba_player_id_to_db_player_id)} player id mappings from the database.")

    # -------------------------
    # STEP 5: FETCH SEASON STATS FROM NBA API
    # -------------------------

    # This endpoint gives us player season stats across the league for the selected season.
    season_stats_response = leaguedashplayerstats.LeagueDashPlayerStats(
        season=season_to_load
    )

    # Convert the response into a pandas dataframe to access the row easier
    season_stats_dataframe = season_stats_response.get_data_frames()[0]

    print(f"Fetched {len(season_stats_dataframe)} sesaon stat rows from nba_api for {season_to_load}.")

    # -------------------------
    # STEP 6: LOOP THROUGH EACH STATS ROW
    # -------------------------
    for _, row in season_stats_dataframe.iterrows():
        nba_player_id = row["PLAYER_ID"]

        # If the NBA player id is not in our local players table, skip it.
        # Protects from inserting orphaned season stats rows
        if nba_player_id not in nba_player_id_to_db_player_id:
            skipped_count += 1
            continue

        # Get the internal database player id
        db_player_id = nba_player_id_to_db_player_id[nba_player_id]

        # Insert the season stats row into our table.
        # ON CONFLICT makes the script able to be reran
        cursor.execute(
            """
            INSERT INTO player_season_stats (
                player_id,
                season,
                games_played,
                minutes_per_game,
                points_per_game,
                rebounds_per_game,
                assists_per_game,
                steals_per_game,
                blocks_per_game,
                field_goal_pct,
                three_point_pct,
                free_throw_pct
            )
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            ON CONFLICT (player_id, season)
            DO UPDATE SET
                games_played = EXCLUDED.games_played,
                minutes_per_game = EXCLUDED.minutes_per_game,
                points_per_game = EXCLUDED.points_per_game,
                rebounds_per_game = EXCLUDED.rebounds_per_game,
                assists_per_game = EXCLUDED.assists_per_game,
                steals_per_game = EXCLUDED.steals_per_game,
                blocks_per_game = EXCLUDED.blocks_per_game,
                field_goal_pct = EXCLUDED.field_goal_pct,
                three_point_pct = EXCLUDED.three_point_pct,
                free_throw_pct = EXCLUDED.free_throw_pct
            """,
            (
                db_player_id,
                season_to_load,
                row["GP"],       # Games Played
                row["MIN"],      # Minutes Per Game
                row["PTS"],      # Points Per Game
                row["REB"],      # Rebounds Per Game
                row["AST"],      # Assists Per Game
                row["STL"],      # Steals Per Game
                row["BLK"],      # Blocks Per Game
                row["FG_PCT"],   # Field Goal Percentage
                row["FG3_PCT"],  # Three Point Percentage
                row["FT_PCT"],   # Free Throw Percentage
            ),
        )

        # increment the inserted or updated counter
        inserted_or_updated_count += 1

    # -------------------------
    # STEP 7: COMMIT CHANGES
    # -------------------------

    connection.commit()

    print(f"Inserted or updated {inserted_or_updated_count} player season stat rows.")
    print(f"Skipped {skipped_count} rows because no matching player was found in the local database.")

except Exception as error:
    # Roll back the current transaction if something fails
    if connection is not None:
        connection.rollback()

    # Show the error so we know what went wrong
    print(f"Season stats ETL script failed: {error}")
    raise

finally:
    # Always close the cursor if it was created
    if cursor is not None:
        cursor.close()

    # Always close the connection if it was created
    if connection is not None:
        connection.close()
