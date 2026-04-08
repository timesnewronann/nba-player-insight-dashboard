# HoopIQ

HoopIQ is a full-stack NBA player analytics dashboard built to explore player data, season stats, and game performance through a clean web application.

**GOAL**: build a real-world software engineering portfolio piece that demonstrates backend development, relational database design, data ingestion, API development, and frontend integration.

## Run What We Have So Far

This project currently has:

- a Spring Boot backend scaffold
- a PostgreSQL database created locally
- SQL tables for `teams`, `players`, and `player_season_stats`
- a Python environment for ETL / ingestion work
- a search query

### 1. Activate the Python virtual environment

#### macOS / Linux

```bash
source .venv/bin/activate
```

### 2. Ingest nba_api data

### 3. Run the springboot app

```
cd backend
./mvnw spring-boot:run
```

### 4. connect to Postgres

psql -U postgres -d nba_player_insight

### 5. inside psql, list tables

\dt

## Why I Built This

I wanted to create a project that connects my love of the game with the kinds of technical skills used in software engineering.

HoopIQ will allow the user to:

- search NBA players
- view player profile information
- view season stats
- compare players
- explore trends and performance insights

## Project Goals:

MVP goal: build a hosted application where user can:

- search for NBA players
- view player details
- view season-level stats
- compare two players

I plan to extend the project with more advanced analytics and specializations.

# Tech Stack

### Backend

- Java
- Spring Boot
- Spring Data JPA

### Databse

- PostgreSQL

### Data Ingestion / ETL

- Python
- nba_api

### Frontend

- React

### Tooling

- Git
- GitHub
- Postman
- pgAdmin / psql

## Architecture Overview

```text
NBA data source
    -> Python ingestion scripts
    -> PostgreSQL database
    -> Spring Boot REST API
    -> React Frontend
```
