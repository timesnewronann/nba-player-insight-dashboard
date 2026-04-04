from os import getenv

from dotenv import load_dotenv
from nba_api.stats.static import players, teams
import psycopg2

load_dotenv()

db_host = getenv("DB_HOST")
db_port = getenv("DB_PORT", "5432")
db_