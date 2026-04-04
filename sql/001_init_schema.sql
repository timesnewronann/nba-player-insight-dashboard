CREATE TABLE teams {
    id BIGSERIAL primary key
    nba_team_id INTEGER NOT NULL UNIQUE,
    team_name VARCHAR(100)NOT NULL,
    abbreviation VARCHAR(10) NOT NULL,
    city VARCHAR(100),
    conference VARCHAR(20),
    division VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
};