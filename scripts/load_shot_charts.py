from os import getenv
from pathlib import Path


# Load variables form the .env file
from dotenv import load_dotenv

# Static NBA data helpers for players
from nba_api.stats.static import players, teams

from nba_api.stats.endpoints import shotchartdetail

# PostgreSQL driver for Python
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
db_port = getenv("DB_PORT", "5432")
db_name = getenv("DB_NAME")
db_user = getenv("DB_USER")
db_password = getenv("DB_PASSWORD")

print(f"Using env file: {env_path}")
print(f"db_host: {db_host}")
print(f"db_user: {db_user}")


# Fail early if required environment variables are missing
if not db_host or not db_name or not db_user or not db_password:
    raise ValueError("Missing one or more required datbase environment variables.")

# -------------------------
# STEP 1: CHOOSE THE SEASON WE WANT TO LOAD
# -------------------------
season_to_load = "2025-26"

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

    # Only fetch players who have game logs but no shot chart data yet
    # This makes re-runs fast — already-loaded players are skipped entirely
    cursor.execute(
        """
        SELECT DISTINCT p.id, p.nba_player_id
        FROM players p
        JOIN player_game_logs gl ON p.id = gl.player_id
        WHERE p.active = TRUE
          AND NOT EXISTS (SELECT 1 FROM shot_chart sc WHERE sc.player_id = p.id)
        """
    )

    # get all the rows for players missing shot data
    active_player_rows = cursor.fetchall()

    # use a dictionary to provide fast lookup
    nba_player_id_to_db_player_id = {}

    for db_player_id, nba_player_id in active_player_rows:
        nba_player_id_to_db_player_id[nba_player_id] = db_player_id

    print(f"Loaded {len(nba_player_id_to_db_player_id)} players missing shot data")

    # -------------------------
    # STEP 4.5: BUILD TEAMS ID LOOKUP DICTIONARY
    # ------------------------
    cursor.execute(
        """
        SELECT id, nba_team_id FROM teams
        """
    )

    # grab all the rows
    team_rows = cursor.fetchall()

    # dictionary for fast lookup
    nba_team_id_to_db_team_id = {}

    for db_team_id, nba_team_id in team_rows:
        nba_team_id_to_db_team_id[nba_team_id] = db_team_id

    db_team_id_to_nba_team_id = {}

    for db_team_id, nba_team_id in team_rows:
        nba_team_id_to_db_team_id[nba_team_id] = db_team_id
        db_team_id_to_nba_team_id[db_team_id] = nba_team_id  # reverse lookup

    # Print statement that we were able to successfully map the ids
    print(f"Loaded {len(nba_team_id_to_db_team_id)} team id mappings from the database")

    # -------------------------
    # STEP 4.8 BUILD GAMES ID LOOKUP DICTIONARY
    # ------------------------
    cursor.execute(
        """
        SELECT id, nba_game_id FROM games
        """
    )

    game_rows = cursor.fetchall()

    # dictionary for fast lookup
    nba_game_id_to_db_game_id = {}

    for db_game_id, nba_game_id in game_rows:
        nba_game_id_to_db_game_id[nba_game_id] = db_game_id

    # Print statement that we were able to successfully map the ids
    print(f"Loaded {len(nba_game_id_to_db_game_id)} game id mappings from the database")

    # -------------------------
    # STEP 5: FETCH SHOT CHART STATS FROM NBA API

    # -------------------------
    total = len(nba_player_id_to_db_player_id)
    for i, (nba_player_id, db_player_id) in enumerate(nba_player_id_to_db_player_id.items(), 1):
        print(f"[{i}/{total}] Fetching shots for player {nba_player_id}")

        # A player may have been on multiple teams during the season (trades).
        # The ShotChartDetail API scopes results to one team at a time, so we
        # call it once per team the player appeared for in our game logs.
        cursor.execute(
            """
            SELECT DISTINCT t.nba_team_id
            FROM player_game_logs gl
            JOIN teams t ON gl.team_id = t.id
            WHERE gl.player_id = %s
            """,
            (db_player_id,)
        )
        player_team_ids = [row[0] for row in cursor.fetchall()]

        for nba_team_id in player_team_ids:
            try:
                shot_chart_response = shotchartdetail.ShotChartDetail(
                    player_id=nba_player_id,
                    team_id=nba_team_id,
                    season_nullable=season_to_load,
                    context_measure_simple="FGA"
                )

                time.sleep(1)

            except Exception as e:
                print(f"  Skipping player {nba_player_id} / team {nba_team_id} due to API error: {e}")
                continue

            # get the dataframe from the response
            shot_chart_df = shot_chart_response.get_data_frames()[0]

            for _, row in shot_chart_df.iterrows():
                nba_game_id = int(row["GAME_ID"])
                db_game_id = nba_game_id_to_db_game_id.get(nba_game_id)
                if not db_game_id:
                    skipped_count += 1
                    continue

                cursor.execute(
                    """
                    INSERT INTO shot_chart (
                        player_id, game_id, game_event_id, loc_x, loc_y,
                        shot_made, shot_type, shot_zone, game_date
                    )
                    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
                    ON CONFLICT (player_id, game_id, game_event_id) DO NOTHING
                    """,
                    (
                        db_player_id,
                        db_game_id,
                        int(row["GAME_EVENT_ID"]),
                        row["LOC_X"],
                        row["LOC_Y"],
                        bool(row["SHOT_MADE_FLAG"]),
                        row["SHOT_TYPE"],
                        row["SHOT_ZONE_BASIC"],
                        row["GAME_DATE"]
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
