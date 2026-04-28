from os import getenv
from pathlib import Path

# Load variables from .env file
from dotenv import load_dotenv

# player game log stats endpoint -> returns player game log columns in one request
from nba_api.stats.endpoints import playergamelog

# PosstgreSQL driver for Python
import psycopg2

# Add delay between api calls to not destroy the server
import time

# -------------------------
# STEP 0: LOAD ENVIRONMENT VARIABLES
# -------------------------

# Load values from .env so we can read DB credentials safely
env_path = Path(__file__).resolve().parent.parent / ".env"
load_dotenv(dotenv_path=env_path, override=True)

# Read the database configurations from our env file
db_host = getenv("DB_HOST")
db_port = getenv("DB_PORT", "5432")  # if DB PORT isn't set use 5432
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
# Use 2024-25 season
season_to_load = "2024-25"

# -------------------------
# STEP 2: PREP VARIABLES
# -------------------------

# Initialize connection and cursor to None
connection = None
cursor = None

# Initialize counters
inserted_or_updated_count = 0
skipped_count = 0

# Open try block
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

    # Create the cursor
    cursor = connection.cursor()

    # -------------------------
    # STEP 4: BUILD PLAYER ID LOOKUP DICTIONARY
    # -------------------------

    # Translate nba_player_id into our databse's internal player_id
    cursor.execute(
        """
        SELECT id, nba_player_id
        FROM players
        WHERE active = TRUE
        """
    )

    # get all the rows
    active_players_rows = cursor.fetchall()

    # use a dictionary to provide fast lookup
    nba_player_id_to_db_player_id = {}

    # build the dictionary
    for db_player_id, nba_player_id in active_players_rows:
        # translate the player id key to player_id
        nba_player_id_to_db_player_id[nba_player_id] = db_player_id

    # Print statement that we were able to successfully map the ids
    print(f"Loaded {len(nba_player_id_to_db_player_id)} player id mappings from the database")

    # -------------------------
    # STEP 4.5: BUILD TEAMS ID LOOKUP DICTIONARY
    # -------------------------
    cursor.execute(
        """
        SELECT id, abbreviation FROM teams
        """
    )

    # grab all the rows
    team_abbrev_rows = cursor.fetchall()

    # dictionary for fast lookup
    abbreviation_to_db_team_id = {}

    for db_team_id, abbreviation in team_abbrev_rows:
        abbreviation_to_db_team_id[abbreviation] = db_team_id

    # Print statement that we were able to successfully map the ids
    print(f"Loaded {len(abbreviation_to_db_team_id)} team id mappings from the database")

    # -------------------------
    # STEP 5: FETCH SEASON STATS FROM NBA API
    # -------------------------
    for nba_player_id, db_player_id in nba_player_id_to_db_player_id.items():
        # fetch game logs from API for this player
        # loop through each game log row and insert
        # Wrapped api call in a try except so we don't timeout on every player
        # We will get some players skipped though
        try:
            game_log_response = playergamelog.PlayerGameLog(
                player_id=nba_player_id,
                season=season_to_load
            )

            time.sleep(1)
        except Exception as e:
            print(f"Skipping player {nba_player_id} due to error: {e}")
            continue

        # convert into a dataframe
        game_log_df = game_log_response.get_data_frames()[0]
        for _, row in game_log_df.iterrows():
            # read nba_team_id and nba_game_id from the row
            nba_game_id = row["Game_ID"]

            team_abbreviation = row["MATCHUP"][0:3]

            # translate team abbreviation or skip if not found
            if team_abbreviation not in abbreviation_to_db_team_id:
                skipped_count += 1
                continue

            db_team_id = abbreviation_to_db_team_id[team_abbreviation]

            # insert into games table
            cursor.execute(
                """
                INSERT INTO games (
                    nba_game_id,
                    season
                )
                VALUES(%s, %s)
                ON CONFLICT (nba_game_id) 
                DO NOTHING
                """,
                (
                    nba_game_id,
                    season_to_load,

                )
            )

            # get internal game id
            cursor.execute(
                """
                SELECT id FROM games WHERE nba_game_id = %s
                """,
                (
                    nba_game_id,
                )
            )

            # grab the id
            game_row = cursor.fetchone()
            db_game_id = game_row[0]

            # insert into player_game_logs
            cursor.execute(
                """
            INSERT INTO player_game_logs(
                player_id,
                game_id,
                team_id,
                pts_scored,
                assists,
                rebounds,
                blocks,
                steals,
                minutes,
                field_goal_pct,
                three_point_pct,
                free_throw_pct,
                turnovers,
                win_loss
            )
            VALUES(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            ON CONFLICT (player_id, game_id)
            DO UPDATE SET
                    pts_scored = EXCLUDED.pts_scored,
                    assists = EXCLUDED.assists,
                    rebounds = EXCLUDED.rebounds,
                    blocks = EXCLUDED.blocks,
                    steals = EXCLUDED.steals,
                    minutes = EXCLUDED.minutes,
                    field_goal_pct = EXCLUDED.field_goal_pct,
                    three_point_pct = EXCLUDED.three_point_pct,
                    free_throw_pct = EXCLUDED.free_throw_pct,
                    turnovers = EXCLUDED.turnovers,
                    win_loss = EXCLUDED.win_loss
            """,
                (
                    db_player_id,
                    db_game_id,
                    db_team_id,
                    row["PTS"],
                    row["AST"],
                    row["REB"],
                    row["BLK"],
                    row["STL"],
                    row["MIN"],
                    row["FG_PCT"],
                    row["FG3_PCT"],
                    row["FT_PCT"],
                    row["TOV"],
                    row["WL"]
                )
            )

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
