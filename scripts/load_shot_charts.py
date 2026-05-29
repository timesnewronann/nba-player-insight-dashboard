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

    # Translate the nba_player_id into our database's internal player_id
    cursor.execute(
        """
        SELECT id, nba_player_id, team_id
        FROM players
        WHERE active = TRUE AND team_id IS NOT NULL
        """
    )

    # you cannot call the shot chart api without a team_id

    # get all the active rows
    active_player_rows = cursor.fetchall()

    # use a dictionary to provide fast lookup
    nba_player_id_to_db_player_id = {}

    # build the dictionary
    for db_player_id, nba_player_id, db_team_id in active_player_rows:
        # translate the player id key to player_id
        nba_player_id_to_db_player_id[nba_player_id] = (db_player_id, db_team_id)

    # Print statement that we were able to successfully map the ids
    # Print statement that we were able to successfully map the ids
    print(f"Loaded {len(nba_player_id_to_db_player_id)} player id mappings from the database")

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
    # for nba_player_id, (db_player_id, db_team_id) in nba_player_id_to_db_player_id.items():
    for nba_player_id, (db_player_id, db_team_id) in list(nba_player_id_to_db_player_id.items()):
        # get the nba_team_id for this player using the teams lookup
        # hint: db_team_id is your internal team id, but the API needs nba_team_id
        # how do you translate internal team id to nba_team_id?
        nba_team_id = db_team_id_to_nba_team_id.get(db_team_id)
        if not nba_team_id:
            skipped_count += 1
            continue

        try:
            shot_chart_response = shotchartdetail.ShotChartDetail(
                player_id=nba_player_id,
                team_id=nba_team_id,
                season_nullable=season_to_load
            )

            time.sleep(1)

        except Exception as e:
            print(f"Skipping player {nba_player_id} due to error: {e}")
            continue

        # get the dataframe from the response
        shot_chart_df = shot_chart_response.get_data_frames()[0]

        # loop through each shot row
        # for each shot:
        for _, row in shot_chart_df.iterrows():
            nba_game_id = int(row["GAME_ID"])
            # note uppercase
            # print(
            #     f"Looking up game id: {nba_game_id}, found: {nba_game_id_to_db_game_id.get(nba_game_id)}")
            # print(f"Sample SHOT_TYPE: {shot_chart_df['SHOT_TYPE'].iloc[0]}")
            # db_game_id = nba_game_id_to_db_game_id.get(nba_game_id)
            if not db_game_id:
                skipped_count += 1
                continue

            cursor.execute(
                """
                INSERT INTO shot_chart (
                    player_id, game_id, loc_x, loc_y,
                    shot_made, shot_type, shot_zone, game_date
                )
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
                ON CONFLICT (player_id, game_id, loc_x, loc_y) DO NOTHING
                """,
                (
                    db_player_id,
                    db_game_id,
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
