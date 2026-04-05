from os import getenv

# Load variables from the .env file
from dotenv import load_dotenv

# Static NBA data helpers for teams and players
from nba_api.stats.static import players, teams

# PostgreSQL driver for Python
import psycopg2


# Load values from .env so we can read DB credentials safely
load_dotenv()

# Read database configuration from environment variables
db_host = getenv("DB_HOST")
db_port = getenv("DB_PORT", "5432")  # Use 5432 by default if DB_PORT isn't set
db_name = getenv("DB_NAME")
db_user = getenv("DB_USER")
db_password = getenv("DB_PASSWORD")

# Fail early if required environment variables are missing
if not db_host or not db_name or not db_user or not db_password:
    raise ValueError("Missing one or more required database environment variables.")

# Start with None so these names exist even if connection setup fails
connection = None
cursor = None

try:
    # Open a connection to PostgreSQL
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
    # STEP 1: LOAD TEAMS
    # -------------------------

    # Get all NBA teams from nba_api
    team_rows = teams.get_teams()

    # Insert or update each team
    for team in team_rows:
        cursor.execute(
            """
            INSERT INTO teams (
                nba_team_id,
                team_name,
                abbreviation,
                city,
                conference,
                division
            )
            VALUES (%s, %s, %s, %s, %s, %s)
            ON CONFLICT (nba_team_id)
            DO UPDATE SET
                team_name = EXCLUDED.team_name,
                abbreviation = EXCLUDED.abbreviation,
                city = EXCLUDED.city,
                conference = EXCLUDED.conference,
                division = EXCLUDED.division
            """,
            (
                team["id"],
                team["full_name"],
                team["abbreviation"],
                team["city"],
                team["conference"],
                team["division"],
            ),
        )

    # -------------------------
    # STEP 2: LOAD PLAYERS
    # -------------------------

    # Get all NBA players from nba_api
    player_rows = players.get_players()

    # Insert or update each player
    for player in player_rows:
        cursor.execute(
            """
            INSERT INTO players (
                nba_player_id,
                first_name,
                last_name,
                full_name,
                team_id,
                position,
                height,
                weight,
                active
            )
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
            ON CONFLICT (nba_player_id)
            DO UPDATE SET
                first_name = EXCLUDED.first_name,
                last_name = EXCLUDED.last_name,
                full_name = EXCLUDED.full_name,
                active = EXCLUDED.active
            """,
            (
                player["id"],
                player["first_name"],
                player["last_name"],
                player["full_name"],
                None,  # We are not filling team_id yet
                None,  # We are not filling position yet
                None,  # We are not filling height yet
                None,  # We are not filling weight yet
                player["is_active"],
            ),
        )

    # Save all successful changes
    connection.commit()

    # Print a success summary
    print(f"Loaded or updated {len(team_rows)} teams.")
    print(f"Loaded or updated {len(player_rows)} players.")

except Exception as error:
    # Roll back the current transaction if something fails
    if connection is not None:
        connection.rollback()

    # Show the error so we know what went wrong
    print(f"ETL script failed: {error}")
    raise

finally:
    # Always close the cursor if it was created
    if cursor is not None:
        cursor.close()

    # Always close the connection if it was created
    if connection is not None:
        connection.close()
