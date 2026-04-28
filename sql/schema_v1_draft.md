# Schema V1 Draft

## teams

- id (BIGSERIAL, primary key)
- nba_team_id (INTEGER, unique, not null)
- team_name (VARCHAR(100), not null)
- abbreviation (VARCHAR(10), not null)
- city (VARCHAR(100))
- conference (VARCHAR(20))
- division (VARCHAR(20))
- created_at (TIMESTAMP, default current timestamp)

## players

- id (BIGSERIAL, primary key)
- nba_player_id (INTEGER, unique, not null)
- first_name (VARCHAR(100), not null)
- last_name (VARCHAR(100), not null)
- full_name (VARCHAR(200), not null)
- team_id (BIGINT, foreign key -> teams.id)
- position (VARCHAR(20))
- height (VARCHAR(20))
- weight (VARCHAR(20))
- active (BOOLEAN, default true)
- created_at (TIMESTAMP, default current timestamp)

## player_season_stats

- id (BIGSERIAL, primary key)
- player_id (BIGINT, foreign key -> players.id, not null)
- season (VARCHAR(20), not null)
- games_played (INTEGER)
- minutes_per_game (NUMERIC(5,2))
- points_per_game (NUMERIC(5,2))
- rebounds_per_game (NUMERIC(5,2))
- assists_per_game (NUMERIC(5,2))
- steals_per_game (NUMERIC(5,2))
- blocks_per_game (NUMERIC(5,2))
- field_goal_pct (NUMERIC(5,3))
- three_point_pct (NUMERIC(5,3))
- free_throw_pct (NUMERIC(5,3))
- created_at (TIMESTAMP, default current timestamp)
- unique(player_id, season)

## games

- id (BIGSERIAL, primary key)
- nba_game_id (INTEGER, not null)
- home_team_id (BIGINT, foreign key -> teams.id, nullable)
- away_team_id (BIGINT, foreign key -> teams.id, nullable)
- home_team_score (INTEGER, nullable)
- away_team_score (INTEGER, nullable)
- game_date (DATE, nullable)
- season (VARCHAR(20), not null)
- created_at (TIMESTAMP, default current timestamp)

## player_game_logs

- id (BIGSERIAL, primary key)
- player_id (BIGINT, foreign key -> players.id, not null)
- game_id (BIGINT, foreign key -> games.id, not null)
- team_id (BIGINT, foreign key -> teams.id, not null)
- pts_scored (INTEGER, not null)
- assists (INTEGER, not null)
- rebounds (INTEGER, not null)
- blocks (INTEGER, not null)
- steals (INTEGER, not null)
- minutes (NUMERIC, not null)
- field_goal_pct (NUMERIC(5,3))
- three_point_pct (NUMERIC(5,3))
- free_throw_pct (NUMERIC(5,3))
- turnovers (INTEGER, not null)
- win_loss (CHAR(1))
- created_at (TIMESTAMP, default current timestamp)
- unique(player_id, game_id)
