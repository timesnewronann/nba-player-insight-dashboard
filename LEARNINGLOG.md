# Learning Log

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