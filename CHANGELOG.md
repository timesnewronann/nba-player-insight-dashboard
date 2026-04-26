# Change Log

All notable changes to this project will be documented in this file.

The format is based on Keep a Changelog and this project adheres to Semantic Versioning.

## [Unreleased] - yyyy-mm-dd

Here we write upgrading notes for brands. It's a team effort to make them as straightforward as possible.

Added

- PROJECTNAME-XXXX MINOR Ticket title goes here.
- PROJECTNAME-YYYY PATCH Ticket title goes here.
  Changed

Fixed

## [1.0.6] - 04-26-2026

## Added

- games and player_games_logs tables to PostgreSQl
- PlayerGameStat, Game, Team Entity files
- PlayerGameStatRepository
- GET /api/players/{id}/games endpoint

### Changed

- games table columns home_team_id, away_team_id,
  home_team_score, away_team_score, game_date
  made nullable temporarily pending full games ETL

## [1.0.5] - 04-13-2026

### Future Improvements

- Load more than one season
- Eventually load 2025-26 regular season data
- Possibly parameterize season_to_load
- Possibly parameterize season_type_all_star and playoffs

### Added

- player_season_stats.py
- player_season_stats data to the data table

### Fixed

- Missing data from player_season_stats data table

## [1.0.4] - 04-07-2026

### Added

- added a player search endpoint at /api/players/{id}
- example: /api/players/1

## [1.0.3] - 04-06-2026

### Added

- added player search endpoint at /api/players/search
- added PlayerService to separate controller and repository responsibilities
- added custom repository query for searching full name, first name, or last name

### Changed

- PlayerController.java
- PlayerRepository.java
- PlayerService.java

## [1.0.2] - 04-05-2026

### Added

- Spring Boot PostgreSQL connection configuration
- `Player` JPA entity mapped to the `players` table
- `PlayerRepository` for database access
- `GET /api/players` endpoint for returning player data as JSON

### Verified

- backend successfully connected to PostgreSQL 15
- `/api/players` returned persisted NBA player records

## [1.0.1] - 04-05-2026

Implemented an ETL script which reads configuration, connects to the nba_insight database, pulls external data from the nba_api, inserts the data into our schema, and can be rerun safely.

### Added

- initial Python ETL script to load NBA teams and players into PostgreSQL
- environment variable support for local databse configuration
- upsert logic for rerunnable team and player ingestion

### Changed

- updated project README with current setup and ETL workflows notes

### Fixed

- resolved local PostgreSQL role and environment variables loading issues
- handled missing team fields from `nba_api` by storing nullable values

## [1.0.0] - 04-03-2026

Defined the project concept and MVP scope

### Added

- Set up the Spring Boot backend project
- Created the PostgreSQL database
- Designed and created the first schema tables:
  - teams
  - players
  - player_season_stats
