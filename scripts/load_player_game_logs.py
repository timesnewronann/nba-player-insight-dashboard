# Import our necessary libraries
from os import getenv
from pathlib import Path

# player game logs endpoint -> returns an individual player's many game logs
from nba_api.stats.endpoints import leaguedashplayerstats

# PostgreSQL driver for Python
import psycopg2

# 1. Load environment variables from .env
from dotenv import load_dotenv

# -------------------------
# STEP 0: LOAD ENVIRONMENT VARIABLES
# -------------------------

# Load values from .env so we can read DB credentials safely
env_path = Path(__file__).resolve().parent.parent / ".env"

# 2. Connect to the Postgres database

# 3. Build Player Lookup dictionary
#   nba_player_id -> players.id

# 4. Build Team Lookup dictionary
#   nba_team_id -> teams.id

# 5. Select the season we want to load (2024-25)

# 6. Fetch game log rows from nba_api

# 7. For each API row:
#   - read nba_player_id
#   - read nba_team_id
#   - read nba_game_id
#   - translate nba_player_id into our internal players.id to match the database
#   - translate nba_team_id into our internal teams.id to match the database
#   - skip row if player or team are missing locally
#   - insert or update the game row in games
#   - get the internal games.id for that nba_game_id
#   - insert or update the player_game_logs row using internal ids


# 8. Commit changes

# 9. If something fails, rollback transition

# 10 Always close cursor and connection
