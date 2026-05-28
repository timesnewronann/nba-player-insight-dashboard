from os import getenv
from pathlib import Path


# 1. Load Environment Variables
from dotenv import load_dotenv

# league-wide common player details
from nba_api.stats.endpoints import commonplayerinfo

# PostgreSQL driver for Python
import psycopg2

import time

# -------------------------
# STEP 1: LOAD ENVIRONMENT VARIABLES
# -------------------------

# Load values from .env so we can read DB credentials safely
env_path = Path(__file__).resolve().parent.parent / ".env"
load_dotenv(dotenv_path=env_path, override=True)

# Read database configuration from environment variables
db_host = getenv("DB_HOST")
db_port = getenv("DB_PORT", "5432")
db_name = getenv("DB_NAME")
db_user = getenv("DB_USER")
db_password = getenv("DB_PASSWORD")

print(f"Using env file: {env_path}")
print(f"db_host: {db_host}")
print(f"db_user: {db_user}")

# Fail early if required environment varaibles are missing
if not db_host or not db_name or not db_user or not db_password:
    raise ValueError("Missing one or more required database environment variables.")


# start with None so these names exist even if connection setup fails
connection = None
cursor = None

# Track what occurred after script runs
inserted_or_updated_count = 0
skipped_count = 0

# 3. Query active players from database - get id and nba_player_id for each
try:

    # -------------------------
    # STEP 2: CONNECT TO POSTGRES
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

    cursor.execute(
        """
        SELECT id, nba_player_id
        FROM players
        WHERE active = TRUE
        """
    )

    # get all the rows
    active_player_rows = cursor.fetchall()

    # Use a dictionary to provide fast lookup
    nba_player_id_to_db_player_id = {}

    # build the dictionary
    for db_player_id, nba_player_id in active_player_rows:
        # translate the player id key to player_id
        nba_player_id_to_db_player_id[nba_player_id] = db_player_id

    # Print statement that we were able to successfully map the ids
    print(f"Loaded {len(nba_player_id_to_db_player_id)} player id mappings from the databse")

    # -------------------------
    # STEP 4.5: BUILD TEAMS ID LOOKUP DICTIONARY
    # ------------------------
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

    # Print Statement that we were able to successfully map the ids
    print(f"Loaded {len(abbreviation_to_db_team_id)} team id mappings from the database")

    # -------------------------
    # STEP 5: FETCH PLAYER INFO FROM NBA API
    # -------------------------
    # build the dictionary
    for db_player_id, nba_player_id in active_player_rows:
        try:
            player_info = commonplayerinfo.CommonPlayerInfo(player_id=nba_player_id)
            time.sleep(1)
        except Exception as e:
            print(f"Skipping player {nba_player_id} due to error: {e}")
            continue

        # Parse the response
        info = player_info.get_data_frames()[0].iloc[0]

        # Access fields
        # read position, height, weight, team_abbreviation from the rows
        position = info["POSITION"]
        height = info["HEIGHT"]
        weight = info["WEIGHT"]
        team_abbreviation = info["TEAM_ABBREVIATION"]

        # Translate team abbreviation to team_id
        # Free agents may have empty team abbreviationn
        if team_abbreviation and team_abbreviation in abbreviation_to_db_team_id:
            db_team_id = abbreviation_to_db_team_id[team_abbreviation]
        else:
            db_team_id = None


        cursor.execute(
            """
            UPDATE players
            SET position = %s,
                height = %s,
                weight = %s,
                team_id = %s
            WHERE id = %s
            """,
            (position, height, weight, db_team_id, db_player_id)
        )
        inserted_or_updated_count += 1

    # Print statement that we were able to successfully map the ids
    print(f"Loaded {len(nba_player_id_to_db_player_id)} player id mappings from the database")

    connection.commit()
    print(f"Inserted or updated {inserted_or_updated_count} player season stat rows.")
    print(f"Skipped {skipped_count} rows because no matching player was found in the local database.")

except Exception as error:
    if connection is not None:
        connection.rollback()

    # Show the error so we know what went wrong
    print(f"Season stats ETL script failed: {error}")
    raise

finally:
    # Always close the cursor if it was created
    if cursor is not None:
        cursor.close()

    # Always close connection if it was created
    if connection is not None:
        connection.close()
