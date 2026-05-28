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

    # get all the rows
    active_player_rows = cursor.fetchall()

    # use a dictionary to provide fast lookup
    nba_player_id_to_db_player_id = {}

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
