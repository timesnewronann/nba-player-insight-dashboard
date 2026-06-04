# HoopIQ

HoopIQ is a full-stack NBA player analytics dashboard that lets you search players, explore season stats, review game logs, and visualize shot charts — all backed by live NBA data.

**Live app:** https://nba-player-insight-dashboard-timesnewronanns-projects.vercel.app

---

## Features

- **Player search** — real-time search by first name, last name, or full name
- **Player profiles** — headshot from NBA CDN, position, active status
- **Season stats** — PPG, RPG, APG, FG% for the current season
- **Game logs** — per-game results with W/L badges, dates, and FG% colored against season average
- **Shot chart** — D3.js visualization with dots mode (made/missed) and hexbin zone mode showing shooting efficiency by court area
- **Teams** — browse all 30 teams and their rosters

---

## Tech Stack

### Backend
- Java 17, Spring Boot 4
- Spring Data JPA + Hibernate
- Spring Actuator (health monitoring)
- Deployed on **Railway**

### Database
- PostgreSQL (Railway)
- Tables: `teams`, `players`, `player_season_stats`, `games`, `player_game_logs`, `shot_chart`

### Data Ingestion
- Python 3, `nba_api`, `psycopg2`, `python-dotenv`
- One-time ETL scripts in `scripts/`

### Frontend
- React 19, TypeScript, Vite
- Tailwind CSS v4 with custom color palette
- D3.js + d3-hexbin for shot chart rendering
- React Router v7
- Deployed on **Vercel**

---

## Architecture

```
stats.nba.com (nba_api)
    └── Python ETL scripts (run once locally)
            └── Railway PostgreSQL
                    └── Spring Boot REST API  ←→  React + TypeScript (Vercel)
```

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/players/search?query={name}` | Search players by name |
| GET | `/api/players/{id}` | Get a player by database ID |
| GET | `/api/players/{id}/season-stats` | Get season stats for a player |
| GET | `/api/players/{id}/games` | Get game logs for a player |
| GET | `/api/players/{id}/shots` | Get shot chart data for a player |
| GET | `/api/teams` | Get all teams |
| GET | `/api/teams/{id}` | Get a team by ID |
| GET | `/api/teams/{id}/players` | Get all players on a team |

Base URL: `https://nba-player-insight-dashboard-production.up.railway.app`

---

## Local Development

### Prerequisites
- Java 17+
- Node.js 18+
- PostgreSQL (local instance)
- Python 3.11+ with pip

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

Runs on `http://localhost:8080`. Requires a local PostgreSQL database. Set connection details in `backend/src/main/resources/application.properties`.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Runs on `http://localhost:5173`. Create a `.env.local` file in `frontend/` with:

```
VITE_API_URL=http://localhost:8080
```

---

## Data Ingestion (ETL Scripts)

Scripts live in `scripts/`. They pull data from the NBA API and load it into PostgreSQL. Run them once in order to seed the database.

### Setup

```bash
cd scripts
source venv/bin/activate
pip install -r requirements.txt
```

Create a `.env` file in the project root with your database credentials:

```
DB_HOST=your_host
DB_PORT=5432
DB_NAME=your_db
DB_USER=your_user
DB_PASSWORD=your_password
```

### Run scripts in order

```bash
python load_teams_players.py       # loads 30 teams and all players
python load_player_details.py      # populates position, height, weight, team
python load_player_season_stats.py # loads 2025-26 per-game season averages
python load_player_game_logs.py    # loads per-game logs for all active players
python load_shot_charts.py         # loads shot-by-shot location data (~132k rows)
```

Each script is safe to re-run — all inserts use `ON CONFLICT ... DO UPDATE` or `DO NOTHING`.

> `load_player_game_logs.py` and `load_shot_charts.py` make one API call per player and take 15–40 minutes to complete. This is expected — the NBA API rate limits requests.

---

## Deployment

### Backend → Railway

1. Push to GitHub — Railway auto-deploys from the `main` branch
2. Set environment variables in Railway:
   - `DATABASE_URL` — Railway PostgreSQL connection string
   - `CORS_ALLOWED_ORIGINS` — comma-separated list of allowed frontend origins (supports `https://*.vercel.app`)

The `backend/railway.toml` configures the build command, start command, and healthcheck path (`/actuator/health`).

### Frontend → Vercel

1. Connect the GitHub repo in Vercel, set **Root Directory** to `frontend`
2. Set environment variables in Vercel:
   - `VITE_API_URL` — full Railway backend URL, e.g. `https://nba-player-insight-dashboard-production.up.railway.app`
3. Vercel picks up `frontend/vercel.json` automatically, which rewrites all routes to `index.html` for React Router

---

## Why I Built This

I wanted a project that connects my interest in basketball with the technical skills used in real-world software engineering — backend API design, relational database modeling, data pipelines, and modern frontend development. HoopIQ is that project end to end, from ETL scripts pulling raw NBA data to a deployed app with interactive visualizations.
