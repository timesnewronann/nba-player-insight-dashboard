# Imports/ calls existing single-season logic from load_player_season_stats.py
# Do not need to rewrite the lookup dict or the insert logic just call the function per season
from load_player_season_stats import load_player_season_stats

# Queries Database for missing seasons
from os import getenv
from pathlib import Path

# Load variables from the .env file
from dotenv import load_dotenv

# league-wide player season stats endpoint -> returns many players in one request
from nba_api.stats.endpoints import leaguedashplayerstats


# PSUEDOCODE

# Import
from os import getenv
from pathlib import Path

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


def main():
    # 1. Open one Database connection
    connection = psycopg2.connect(
        host=db_host,
        port=db_port,
        dbname=db_name,
        user=db_user,
        password=db_password,
    )
    # 2. Open a cursor from that connection -> run SELECT DISTINC season FROM player_season_stats -> get loaded_seasons
    try:
        cursor = connection.cursor()
        cursor.execute(
            """
            SELECT DISTINCT season 
            FROM player_season_stats
            """
        )

        # get loaded -> fetchall returns a list of tuples
        loaded_season_rows = cursor.fetchall()

        loaded_seasons = []

        # pull the value out of the tuple before appending it.
        for season, in loaded_season_rows:
            loaded_seasons.append(season)

        # 3. Diff agaisnt your full season list -> missing_seasons
        # we need to get all seasons
        # missing_seasons = [s for s in all_seasons if s not in loaded_seasons]
        missing_seasons = [s for s in all_seasons if s not in loaded_seasons]

        # 4. Loop missing_seasons, try/except calling load_player_season_stats(season, connection)
        # passing the same connection opened in step 1
        # try / except call load_player_season_stats
        for missing_season in missing_seasons:
            load_player_season_stats(missing_season, connection)

    # need an except block
    except Exception as error:
        if connection is not None:
            connection.rollback()

        # Show the error so we know what went wrong
        print(f"load player season stats failed: {error}")

    # 5.  Close the connection once after the loop
    finally:
        if cursor is not None:
            cursor.close()


if __name__ == "__main__":
    main()
