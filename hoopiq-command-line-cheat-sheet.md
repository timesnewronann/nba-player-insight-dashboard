# HoopIQ Command Line Cheat Sheet

A reference for the commands you use most while working on the HoopIQ project.

---

## 1. Project navigation

### Go to the project root

```bash
cd /Users/timesnewronan/nba-player-insight-dashboard
```

### Go to the backend folder

```bash
cd /Users/timesnewronan/nba-player-insight-dashboard/backend
```

### Go to the project root from anywhere nearby

```bash
cd ..
```

---

## 2. Python virtual environment

### Create a virtual environment

```bash
python3 -m venv venv
```

### Activate the virtual environment (You neec to cd /scripts)

```bash
source venv/bin/activate
```

### Deactivate the virtual environment

```bash
deactivate
```

### Install packages from requirements.txt

```bash
pip install -r requirements.txt
```

### Install one package

```bash
pip install nba_api
```

### Save installed packages to requirements.txt

```bash
pip freeze > requirements.txt
```

### Check installed packages

```bash
pip list
```

### Check Python version

```bash
python --version
```

---

## 3. Running Python files

### Run a Python script

```bash
python script_name.py
```

### Example

```bash
python scripts/load_players.py
```

---

## 4. Spring Boot / Java backend

### Go to backend

```bash
cd backend
```

### Run the Spring Boot app

```bash
./mvnw spring-boot:run
```

### Clean and build the backend

```bash
./mvnw clean install
```

### Run tests

```bash
./mvnw test
```

### Compile without running

```bash
./mvnw compile
```

---

## 5. Local API endpoints

### Get all players

```text
http://localhost:8080/api/players
```

### Search players

```text
http://localhost:8080/api/players/search?query=lebron
```

### Get player by id

```text
http://localhost:8080/api/players/1
```

### Get player season stats by id

```text
http://localhost:8080/api/players/1/season-stats
```

---

## 6. PostgreSQL connection commands

### Open PostgreSQL

```bash
psql -U postgres
```

### Open a specific database

```bash
psql -U postgres -d nba_player_insight
```

### If your Postgres username is your Mac username

```bash
psql -d nba_player_insight
```

### List all databases

```sql
\l
```

### Connect to a database from inside psql

```sql
\c nba_player_insight
```

### List tables

```sql
\dt
```

### Describe one table

```sql
\d players
```

### Quit psql

```sql
\q
```

---

## 7. Common SQL checks

### See all players

```sql
SELECT * FROM players;
```

### See first 10 players

```sql
SELECT * FROM players LIMIT 10;
```

### Count players

```sql
SELECT COUNT(*) FROM players;
```

### Count season stats rows

```sql
SELECT COUNT(*) FROM player_season_stats;
```

### Show players with internal id and NBA id

```sql
SELECT id, nba_player_id, full_name
FROM players
LIMIT 10;
```

### Show season stats rows

```sql
SELECT * FROM player_season_stats LIMIT 10;
```

### Find season stats for one player id

```sql
SELECT *
FROM player_season_stats
WHERE player_id = 1;
```

### Join players and season stats

```sql
SELECT p.full_name, pss.season, pss.points_per_game
FROM player_season_stats pss
JOIN players p ON pss.player_id = p.id
LIMIT 10;
```

---

## 8. Port 8080 troubleshooting

### Check what is using port 8080

```bash
lsof -i :8080
```

### Kill the process using port 8080

```bash
kill -9 <PID>
```

### Example

```bash
kill -9 17300
```

### Stop the currently running Spring Boot app

```bash
Control + C
```

---

## 9. Git basics

### See current branch

```bash
git branch
```

### See current status

```bash
git status
```

### See unstaged changes

```bash
git diff
```

### Stage all changes

```bash
git add .
```

### Stage specific files

```bash
git add backend/src/main/java/com/timesnewronan/hoopiq_api/controller/PlayerController.java
```

### Unstage everything

```bash
git restore --staged .
```

### Commit changes

```bash
git commit -m "Add player detail endpoint"
```

### Push current branch

```bash
git push origin <branch-name>
```

---

## 10. Git branching workflow

### Create and switch to a new branch

```bash
git switch -c feature/player-season-stats-endpoint
```

### Older version of the same command

```bash
git checkout -b feature/player-season-stats-endpoint
```

### Switch to main

```bash
git switch main
```

### Pull latest main

```bash
git pull origin main
```

### Start a fresh feature branch from main

```bash
git switch main
git pull origin main
git switch -c feature/new-feature-name
```

---

## 11. Good daily workflow for HoopIQ

### Start Python work

```bash
source venv/bin/activate
```

### Start backend work

```bash
cd backend
./mvnw spring-boot:run
```

### Check database

```bash
psql -d nba_player_insight
```

Then inside psql:

```sql
\dt
SELECT COUNT(*) FROM players;
SELECT COUNT(*) FROM player_season_stats;
```

---

## 12. Good PR flow

### Check branch and status

```bash
git branch
git status
```

### Review changes

```bash
git diff
```

### Stage files

```bash
git add .
```

### Commit

```bash
git commit -m "Add player season stats endpoint"
```

### Push

```bash
git push origin feature/player-season-stats-endpoint
```

---

## 13. Fast mental map

### Python side

- activate venv
- run ETL scripts
- load data into Postgres

### Java / Spring Boot side

- run backend
- test API endpoints
- connect controller -> service -> repository

### PostgreSQL side

- verify data exists
- inspect tables
- debug inserts and joins

### Git side

- create branch
- code feature
- test feature
- commit and push
- open PR

---

## 14. Commands you’ll probably use the most

### Activate Python venv

```bash
source venv/bin/activate
```

### Run backend

```bash
cd backend
./mvnw spring-boot:run
```

### Open Postgres database

```bash
psql -d nba_player_insight
```

### List tables

```sql
\dt
```

### Count players

```sql
SELECT COUNT(*) FROM players;
```

### Count season stats

```sql
SELECT COUNT(*) FROM player_season_stats;
```

### Check current Git branch

```bash
git branch
```

### Check status

```bash
git status
```

### Create a new branch

```bash
git switch -c feature/branch-name
```
