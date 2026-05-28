# Schema Shot chart

id — primary key
player_id — foreign key to players
game_id — foreign key to games
loc_x — x coordinate (integer)
loc_y — y coordinate (integer)
shot_made_flag — boolean
shot_type — varchar (2PT or 3PT)
shot_zone_basic — varchar (zone label)
game_date — date
created_at — timestamp
A UNIQUE constraint on (player_id, game_id, loc_x, loc_y) — prevents duplicate shots
