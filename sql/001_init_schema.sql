CREATE TABLE teams (
    id BIGSERIAL PRIMARY KEY,
    nba_team_id INTEGER NOT NULL UNIQUE,
    team_name VARCHAR(100) NOT NULL,
    abbreviation VARCHAR(10) NOT NULL,
    city VARCHAR(100),
    conference VARCHAR(20),
    division VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE players (
    id BIGSERIAL PRIMARY KEY,
    nba_player_id INTEGER NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    team_id BIGINT REFERENCES teams(id),
    position VARCHAR(20),
    height VARCHAR(20),
    weight VARCHAR(20),
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE player_season_stats (
    id BIGSERIAL PRIMARY KEY,
    player_id BIGINT NOT NULL REFERENCES players(id),
    season VARCHAR(20) NOT NULL,
    games_played INTEGER,
    minutes_per_game NUMERIC(5,2),
    points_per_game NUMERIC(5,2),
    rebounds_per_game NUMERIC(5,2),
    assists_per_game NUMERIC(5,2),
    steals_per_game NUMERIC(5,2),
    blocks_per_game NUMERIC(5,2),
    field_goal_pct NUMERIC(5,3),
    three_point_pct NUMERIC(5,3),
    free_throw_pct NUMERIC(5,3),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (player_id, season)
);