# HoopIQ Daily Commands

The commands you’ll use most often while building HoopIQ.

---

## Python

### Activate the virtual environment
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

### Save packages to requirements.txt
```bash
pip freeze > requirements.txt
```

---

## Spring Boot / Java

### Go to backend
```bash
cd backend
```

### Run the Spring Boot app
```bash
./mvnw spring-boot:run
```

### Compile the backend
```bash
./mvnw compile
```

### Run tests
```bash
./mvnw test
```

---

## PostgreSQL

### Open the project database
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

### Quit PostgreSQL
```sql
\q
```

---

## API URLs

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

## Port 8080 troubleshooting

### Check what is using port 8080
```bash
lsof -i :8080
```

### Kill the process using port 8080
```bash
kill -9 <PID>
```

### Stop the currently running Spring Boot app
```bash
Control + C
```

---

## Git

### Check current branch
```bash
git branch
```

### Check status
```bash
git status
```

### Review changes
```bash
git diff
```

### Stage all changes
```bash
git add .
```

### Unstage everything
```bash
git restore --staged .
```

### Commit changes
```bash
git commit -m "your message here"
```

### Push current branch
```bash
git push origin <branch-name>
```

### Create and switch to a new branch
```bash
git switch -c feature/branch-name
```

### Update main and branch off cleanly
```bash
git switch main
git pull origin main
git switch -c feature/new-feature-name
```

---

## Most common daily flow

### 1. Activate Python
```bash
source venv/bin/activate
```

### 2. Start backend
```bash
cd backend
./mvnw spring-boot:run
```

### 3. Check database
```bash
psql -d nba_player_insight
```

Then inside PostgreSQL:
```sql
\dt
SELECT COUNT(*) FROM players;
SELECT COUNT(*) FROM player_season_stats;
```

### 4. Save your work
```bash
git status
git add .
git commit -m "your commit message"
git push origin <branch-name>
```
