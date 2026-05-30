CREATE TABLE shot_chart (
    id BIGSERIAL PRIMARY KEY,
    player_id BIGINT NOT NULL REFERENCES players(id),
    game_id BIGINT NOT NULL REFERENCES games(id),
    loc_x INTEGER NOT NULL,
    loc_y INTEGER NOT NULL,
    shot_made BOOLEAN NOT NULL,
    shot_type VARCHAR(50) NOT NULL,
    shot_zone VARCHAR(50) NOT NULL,
    game_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (player_id, game_id, loc_x, loc_y)
);