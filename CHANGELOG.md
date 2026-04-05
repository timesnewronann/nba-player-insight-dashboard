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

## [1.0.1] - 04-05-2026

Implemented an ETL script which reads configuration, connects to the nba_insight database, pulls external data from the nba_api, inserts the data into our schema, and can be rerun safely.

Added

- load_teams_players.py

## [1.0.0] - 04-03-2026

Defined the project concept and MVP scope

## Added

- Set up the Spring Boot backend project
- Created the PostgreSQL database
- Designed and created the first schema tables:
  - teams
  - players
  - player_season_stats
