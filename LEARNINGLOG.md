# Learning Log

## Command Cheat Sheet

# activate venv

source .venv/bin/activate

# install deps

pip install -r requirements.txt

# run Spring Boot app

./mvnw spring-boot:run

# connect to Postgres

psql -U postgres -d nba_player_insight

# inside psql, list tables

\dt

### April 2nd 2026

- Setup the repo
- Installed Postgresql 15
- Setup the nba_player_insight database
- Setup Python Ingestion Environment
  - Create the virtual environment
  - Install packages:
    - nba_api -> pulls NBA data
    - pandas -> Inspect and shape data
    - psycopg2-binary -> Connect Python to Postgres
- Created first schema draft

### April 3rd 2026

- Turned the schema draft into SQL
- Loaded the schema into Postgres
- DDL = Data Definition Language
- Defining Structure:
  - CREATE TABLE
  - ALTER TABLE
  - DROP TABLE
- Different from Data Manipulation:
  - INSERT
  - UPDATE
  - SELECT
  - DELETE
- Primary key uniquely identifies one row in a table
- Example:
  - One specific player row
  - One specific team row
- Foreign key creates a relationship between tables
  - players.team_id -> teams_id
  - player_season_stats.player_id -> players.id
- Constraints are rules the database enforces
  - NOT NULL
  - UNIQUE
  - PRIMARY KEY
  - REFERNECES

# DATA TABLE

```
nba_player_insight=# \dt
                  List of relations
 Schema |        Name         | Type  |     Owner
--------+---------------------+-------+---------------
 public | player_season_stats | table | timesnewronan
 public | players             | table | timesnewronan
 public | teams               | table | timesnewronan
(3 rows)
```

Use \q to quit out of psql

### Completed

- Defined the project concept and MVP scope
- Set up the Spring Boot backend project
- Created the PostgreSQL database
- Designed and created the first schema tables:
  - teams
  - players
  - player_season_stats

### In Progress

- Building the first Python Ingestion scripts
- Loading NBA team and player data into PostgreSQL

### Next Steps

- Write Ingestion script for teams
- Write ingestion script for players
- Verify data in PostgreSQL
- Verify data in PostgreSQL
- Connect Spring Boot to the database
- Build first REST endpoint for player search

### Database Schema So Far

teams: stores one row per NBA team
players: stores one row per NBA player and links each player to a team
player_season_stats: Stores one row per player per season

#### Pull Request notes

- drafted the initial README
- updated changelog entries
- implemented first Python ETL scaffolding
- loaded teams and players data into Postgres
- prepared the project for backend-to-database integration

### ETL plan

1. Connect Python to PostgreSQL
2. Fetch NBA teams from nba_api
3. Insert or update them safely
4. Fetch NBA players from nba_api
5. Insert or update them safely
6. Avoid duplicates when the script runs again

Extract: ask nba_api for teams and players
Transform: reshape that data into the columns your database expects
Load: insert it into Postgres

Why do we load teams first?
Players refernece teams
players.teams_id -> teams.id

If we try to load players first, we wouldn't always know which internal teams.id to attach

### Psuedocode

```
start script

load database settings from environment variables

connect to PostgreSQL

get all teams from nba_api
for each team:
    insert it if missing
    update it if it already exists

query teams table
build a mapping:
    nba_team_id -> internal database id

get all players from nba_api
for each player:
    figure out team_id if possible
    insert player if missing
    update player if already exists

commit transaction
print counts/results
close connection
```

## April 4th 2026

Mental Model:

1. Grab team data from the NBA source
2. Place it into your teams table
3. Grab player data from the NBA source
4. Place it into your players table
5. Save the whole batch

Question 1

Why do we use ON CONFLICT (nba_team_id) instead of only INSERT?
Purpose: Creates upsert behavior
Upsert means:

- insert if row is new
- update if row already exists

Why this matters ?

- This is what makes the ETL script rerunnable

This works because psycopg2 sends your SQL to Postgres and Postgres supports INSERT ... ON CONFLICT ... DO UPDATE agaisnt a unique key like nba_team_id

Question 2

Why are we okay putting None for team_id right now?
We are putting None for team_id as a placeholder because our schema allows these columns to be nullable

We are not pretending that we have data yet

Question 3

What is the difference between connection and cursor?
A database connection is a physical communication channel between a client application and a database server.

A cursor is a databse object used to retrieve and manipulate data from a result set one row at a time

Simplified answer
connection = the live session with PostgreSQL
cursor = the object you use to execute SQL and fetch results through that session

The connection creates cursor instances and the cursor is what executes commands and queries

Question 4

Why is commit() needed?
Commit is needed to make the changes to the datbase permanent
