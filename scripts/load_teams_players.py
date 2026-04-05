from os import getenv

# Load variables from the .env file
from dotenv import load_dotenv

# Static NBA data helpers for teams and players
from nba_api.stats.static import players, teams

# PostgreSQL driver for python
import psycopg2

# Load values from .env so we can read DB credentials safely
load_dotenv()

# Read database configuration from environment variables
db_host = getenv("DB_HOST")
db_port = getenv("DB_PORT", "5432")  # Use 5432 by default if DB_PORT isn't set
db_name = getenv("DB_NAME")
db_user = getenv("DB_USER")
db_password = getenv("DB_PASSWORD")

# If any required database value is missing raise error
if not db_host or not db_name or not db_user or not db_password:
    raise ValueError("Missing one or more required database environment variables.")

# Open a connection to PostgreSQL using the values from .env
connection = psycopg2.connect(
    host=db_host,
    port=db_port,
    name=db_name,
    user=db_user,
    password=db_password,
)

# Open a cursor instance
cursor = connection.cursor()

# -------------------------
# STEP 1: LOAD TEAMS
# -------------------------

# Get all the NBA teams from nba_api
team_rows = team.get_teams()

# Loop through each team and insert it into the teams table
for team in team_rows:
    # excute a command: Insert the teams into the teams table
    cursor.execute(
        """
        INSERT INTO teams(
            nba_team_id,
            team_name,
            abbreviation,
            city,
            conference,
            division
        )
        VALUES (% s, % s, % s, % s, % s, % s)
        ON CONFLICT(nba_team_id)
        DO UDPATE SET
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

# Get all the NBA players
player_rows = players.get_players()

# Loop through each player and insert them into the players table
for player in player_rows:
    cursor.execute(

    )

# Make the changes to the database persistent
connection.commit()

# Close cursor and communication with the database
cursor.close()
connection.close()
