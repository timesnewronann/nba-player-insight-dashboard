# Learning Log

## Command Cheat Sheet

# activate venv

source .venv/bin/activate

# install deps

pip install -r requirements.txt

# run Spring Boot app

./mvnw spring-boot:run

# connect to Postgres

psql -U postgres -d nba_player_insight

# inside psql, list tables

\dt

### April 2nd 2026

- Setup the repo
- Installed Postgresql 15
- Setup the nba_player_insight database
- Setup Python Ingestion Environment
  - Create the virtual environment
  - Install packages:
    - nba_api -> pulls NBA data
    - pandas -> Inspect and shape data
    - psycopg2-binary -> Connect Python to Postgres
- Created first schema draft

### April 3rd 2026

- Turned the schema draft into SQL
- Loaded the schema into Postgres
- DDL = Data Definition Language
- Defining Structure:
  - CREATE TABLE
  - ALTER TABLE
  - DROP TABLE
- Different from Data Manipulation:
  - INSERT
  - UPDATE
  - SELECT
  - DELETE
- Primary key uniquely identifies one row in a table
- Example:
  - One specific player row
  - One specific team row
- Foreign key creates a relationship between tables
  - players.team_id -> teams_id
  - player_season_stats.player_id -> players.id
- Constraints are rules the database enforces
  - NOT NULL
  - UNIQUE
  - PRIMARY KEY
  - REFERNECES

# DATA TABLE

```
nba_player_insight=# \dt
                  List of relations
 Schema |        Name         | Type  |     Owner
--------+---------------------+-------+---------------
 public | player_season_stats | table | timesnewronan
 public | players             | table | timesnewronan
 public | teams               | table | timesnewronan
(3 rows)
```

Use \q to quit out of psql

### Completed

- Defined the project concept and MVP scope
- Set up the Spring Boot backend project
- Created the PostgreSQL database
- Designed and created the first schema tables:
  - teams
  - players
  - player_season_stats

### In Progress

- Building the first Python Ingestion scripts
- Loading NBA team and player data into PostgreSQL

### Next Steps

- Write Ingestion script for teams
- Write ingestion script for players
- Verify data in PostgreSQL
- Verify data in PostgreSQL
- Connect Spring Boot to the database
- Build first REST endpoint for player search

### Database Schema So Far

teams: stores one row per NBA team
players: stores one row per NBA player and links each player to a team
player_season_stats: Stores one row per player per season

#### Pull Request notes

- drafted the initial README
- updated changelog entries
- implemented first Python ETL scaffolding
- loaded teams and players data into Postgres
- prepared the project for backend-to-database integration

### ETL plan

1. Connect Python to PostgreSQL
2. Fetch NBA teams from nba_api
3. Insert or update them safely
4. Fetch NBA players from nba_api
5. Insert or update them safely
6. Avoid duplicates when the script runs again

Extract: ask nba_api for teams and players
Transform: reshape that data into the columns your database expects
Load: insert it into Postgres

Why do we load teams first?
Players refernece teams
players.teams_id -> teams.id

If we try to load players first, we wouldn't always know which internal teams.id to attach

### Psuedocode

```
start script

load database settings from environment variables

connect to PostgreSQL

get all teams from nba_api
for each team:
    insert it if missing
    update it if it already exists

query teams table
build a mapping:
    nba_team_id -> internal database id

get all players from nba_api
for each player:
    figure out team_id if possible
    insert player if missing
    update player if already exists

commit transaction
print counts/results
close connection
```

## April 4th 2026

Mental Model:

1. Grab team data from the NBA source
2. Place it into your teams table
3. Grab player data from the NBA source
4. Place it into your players table
5. Save the whole batch

Question 1

Why do we use ON CONFLICT (nba_team_id) instead of only INSERT?
Purpose: Creates upsert behavior
Upsert means:

- insert if row is new
- update if row already exists

Why this matters ?

- This is what makes the ETL script rerunnable

This works because psycopg2 sends your SQL to Postgres and Postgres supports INSERT ... ON CONFLICT ... DO UPDATE agaisnt a unique key like nba_team_id

Question 2

Why are we okay putting None for team_id right now?
We are putting None for team_id as a placeholder because our schema allows these columns to be nullable

We are not pretending that we have data yet

Question 3

What is the difference between connection and cursor?
A database connection is a physical communication channel between a client application and a database server.

A cursor is a databse object used to retrieve and manipulate data from a result set one row at a time

Simplified answer
connection = the live session with PostgreSQL
cursor = the object you use to execute SQL and fetch results through that session

The connection creates cursor instances and the cursor is what executes commands and queries

Question 4

Why is commit() needed?
Commit is needed to make the changes to the datbase permanent

## April 5th 2026

1. If the script crashes halfway through loading players, what problem could happen without a rollback()?
   If the script crashes halfway through loading players the cursor inserts may not be written properly and our database is corrupted.

Better Answer:

- the transaction can be left in a failed state
- some earlier statements in that transaction may not be safely finalized
- the connection may refuse more commands until you roll back
- you can end uo unsure what actually got persisted versus what did not

A failed command leaves the transaction aborted and you usually need rollback before issuing more commands on that connection.

"The current transaciton is broken, and we need rollback() to reset it safely"

2. Why is finally a good place to close cursor and connection?
   It's good as a last measure to make sure we don't leave our cursor and connection open which may make our database open and unsafe?

- finally runs whether the try block succeeds or fails
- that makes it the safest place for cleanup code
- so even if an error happens, we still close the cursor and connection.
  Guranteed cleanup

## Try

- try to run this risky code
  Database work and API work can fial:
- wrong password
- DB not running
- SQL typo
- network issue
- bad schema mismatch

Exceptions:

- catches an error if anything inside try fails
- we hae a chance to roll back
- print a helpful message
- re-raise the error

Connection.rollback():

- Cancels the unfinished transaction on this connection

Why this matters?
If something failed halfway through, rollback resets the transaction instead of leaving it in a broken state.

1. Start PostgreSQL
   `brew services start postgresql@15`
2. Make sure the database exists
   `psql postgres`
   List databases
   `\l`
3. Make sure the tables exist
   Connect to your project database
   `\c nba_player_insight`
   List tables
   `\dt`
   Exit psql
   `\q`
4. Activate virtual environment
   `source venv/bin/activate`
5. Install requirements if needed
   `pip install -r scripts/requirements.txt`
6. Run the ETL script
   `python scripts/load_teams_players.py`
7. Verify the data in psql

# Get database users

(venv) (base) timesnewronan@Ronans-MacBook-Air-5 scripts % psql postgres
psql (15.17 (Homebrew))
Type "help" for help.

postgres=# \du
List of roles
Role name | Attributes | Member of
---------------+------------------------------------------------------------+-----------
timesnewronan | Superuser, Create role, Create DB, Replication, Bypass RLS | {}

postgres=#

## Difference between schema problems and data-shape problems

### Schema Problem:

- Wrong column name in SQL
- table does not exist
- column does not exist
- not null violation
- foreign key violation

### Data-shape problem

- keyError
- missing dictionary field
- wrong JSON shape
- wrong field name from API response

ETL Script successfully:

- loaded the .env file
- connected to PostgreSQL
- inserted or updated 30 teams
- inserted or updated 5103 players

## How to connect to project database

`\c nba_player_insight`

## How to verify tables

`\dt`

## How to count the rows

SELECT COUNT(_) FROM teams;
SELECT COUNT(_) FROM players;

## How to preview sample rows

- visually confirms the data looks normal

SELECT _ FROM teams LIMIT 5;
SELECT _ FROM players LIMIT 5;

What problem did we solve ?
"How do we get external NBA data into our own relational database in a rerunnable way"

How did we solve it?
We used:

- environment variables for DB config
- Python to fetch and transform data
- SQL upsert logic to avoid duplicates
- transactions with commit() / rollback()
- try / except / finally for safer execution

HoopIQ:

- database schema created
- ETL Pipeline for teams and players
- real NBA data loaded into PostgreSQL

## GOAL: Feature/springboot-player-api

Make Spring Boot connect to PostgreSQL and expose the first endpoint.
`GET /api/players`

Step 1

Configure Spring Boot database connection

Step 2

Create a Player entity

Step 3

Create a PlayerRepository

Step 4

Create a PlayerService or go directly to controller if we want to stay simple first

Step 5

Create PlayerController

Step 6

Test GET /api/players

## What is Pom.xml doing?

pom.xml is Maven's project instruction sheet

It tells Maven:

- what this project is called
- what Java version to use
- what libraries to download
- how to build and run the app

Kind of like the project's dependency contract

# Application.properties Job

- tell Spring Boot how to connect to your PostgreSQL database and how to behave with JPA/Hibernate

We want Spring Boot to:

1. Start successfully
2. Connect to your existing nba_player_insight database
3. Not try to recreate your tables
4. Print useful SQL logs while you learn

What each line is doing
`spring.application.name=hoopiq-api`
Purpose

This names your Spring Boot application.

Why it matters

It helps with logs and general app identity.

It is not the most important config line, but it is good project metadata.

Database connection section
`spring.datasource.url=jdbc:postgresql://localhost:5432/nba_player_insight`
Purpose

This tells Spring where your database lives.

Break it down

- jdbc:postgresql:// → Java’s Postgres connection format
- localhost → the DB is running on your machine
- 5432 → the Postgres port
- nba_player_insight → the database name
  Why this matters

Without this line, Spring would not know what database to connect to.

`spring.datasource.username=timesnewronan`
Purpose

This tells Spring which PostgreSQL role/user to log in as.

Why it matters

This must match the role you discovered actually works on your machine.

We learned earlier that postgres was wrong for your setup.

`spring.datasource.password=YOUR_PASSWORD_HERE`
Purpose

This gives Spring the password for that PostgreSQL role.

Why it matters

If your Postgres role requires password auth, this must be correct.

Important note

For now this is fine for local testing, but later we should move this into environment variables instead of hardcoding it.

`spring.datasource.driver-class-name=org.postgresql.Driver`
Purpose

This tells Spring which database driver class to use.

Why it matters

It makes the Postgres driver explicit.

Spring can often infer this, but I like being explicit while you are learning.

JPA / Hibernate section
`spring.jpa.hibernate.ddl-auto=none`
Purpose

This tells Hibernate:

do not create, update, or drop my schema automatically

Why this is very important

You already created the tables yourself in PostgreSQL.

So we want Spring Boot to use the schema, not try to manage it.

Why this is the safest choice

If we used something like:

- update
- create
- create-drop

Hibernate could try to change your database structure.

That is not what we want right now.

`spring.jpa.show-sql=true`
Purpose

This tells Spring/Hibernate to print SQL queries in the console.

Why it helps

This is great for learning.

When you hit an endpoint later, you will be able to see what SQL the backend is generating.

That helps connect:

- controller call
- repository logic
- actual SQL query
- spring.jpa.properties.hibernate.format_sql=true
  Purpose

This makes the printed SQL more readable.

Why it helps

Without this, SQL logs can look squished and harder to follow.

This is mainly a developer-experience setting.

## First Backend Feature

GOAL:

- Create the first endpoint:
  `GET /api/players`
  Spring Boot should:

1. Query the players table
2. Turn rows into Java objects
3. Return them as JSON

### Mental Model

1. Entity
   A java class that maps to the players table
   "one java object = one row from the table"
2. Repostiory
   A Spring Interface that reads from the database
   "This is the the tool that fetches player rows for us"
3. Controller
   The HTTP Layer
   "this is what exposes /api/players to the browser or curl"

## Why getters and setters exist

They let Spring/Jpa/Jackson:

- read values
- write values
- serialize objects into JSON

## Problem Solved:

- Can the backend read real data from the database and expose it through an API

Why this matters?
This is your first complete backend slice:

- Table exists
- Entity maps to table
- Repository reads rows
- Controller exposes endpoint
- Browser/curl gets JSON back

1. Why does PlayerRepository extend JpaRepository<Player, Long>?
   Because Spring Data JPA gives you built-in database methods like

- findAll()
- findById()
- save()

without you writing the implementation yourself.

So the interface is like a contract, and Spring generates the working repository behind the scenes.

2. Why do we need @Table(name = "players")?

Because the Java class is named player, but the actual SQL table is named players

## Search Player feature

V1 Search:

- search by player name
- support partial matches
- ignore uppercase vs lowercase
- return a small clean response
- sort results alphabetically

` GET /api/players/search?q=lebron

Feature Flow

```
Frontend search box
    ->
GET /api/players/search?q=lebron
    ->
Controller
    ->
Service
    ->
Repository
    ->
PostgreSQL players table
    ->
matching player rows
    ->
JSON response
```

Fllows the layered structure we want in Spring Boot REST apps
Controllers expose HTTP endpoints and repository methods handle database access
Spring's REST guides use @RestController and request mappings for that endpoint layer, and Spring Data JPA supports derived query such as Containing and case-insensitive matching with IgnoreCase

Main Idea:

- Entity = maps Java to the players table
- Repository = talks to the database
- Service = business logic
- Controller = HTTP layer
- DTO = the response we send back

Why use DTO?
Even if your Player entity has many fields, the search screen does not need all of them.

For example, search results probably one need:

- id
- nbaPlayerId
- fullName
- teamId
- position
- active

They let us return only the data the UI needs instead of dumping the whole entity.

# Psuedocode

## Repository

find players where full_name contains the search text
ignore uppercase/lowercase
order by full_name ascending
limit to a reasonable number later if needed

## Service Psuedocode

receive search string q

if q is blank:
return empty list

trim whitespace from q

ask repository for matching players

convert each Player entity into PlayerSearchResultDto

return DTO list

## Controller Psuedocode

listen for GET /api/players/search

read query parameter q
call playerService.searchPlayers(q)

return 200 OK with JSON list

## The Search Method Idea

Spring Data JPA lets you derive queries from repository method names, including Containing and IgnoreCase
`List<Player> findByFullNameContainingIgnoreCaseOrderByFullNameAsc(String fullName);`

- Containing = partial match
- IgnoreCase = "lebron" and "LeBron" both work
- OrderByFullNameAsc = stable alphabetical results

## Psuedocode:

user sends GET /api/players/search?name=lebron

controller receives the "name" query parameter

controller calls repository search method

repository finds players where full_name contains "lebron"
or first_name contains "lebron"
or last_name contains "lebron"

controller returns matching players as JSON

`findByFullNameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase`
Spring Data JPA can generate queries from method names.

- look in fullName
- or firstName
- or lastName
- use contains
- ignore uppercase/lowercase differences

Return players wehre the search text appears in:

- full name
- first name
- last name

Examples:

- lebron
- james
- steph
- curry

Use a custom @Query to avoid the long name of findByFullNameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase
Why is this better?

- method name stays short: searchPlayers
- query logic is explicit
- easier to edit later
- easier to teach and reason about
  Example:

```
@Query("""
    SELECT p
    FROM Player p
    WHERE LOWER(p.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
       OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
       OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
""")
List<Player> searchPlayers(@Param("searchTerm") String searchTerm);
```

## Psuedocode:

1. User sends a search request with a query string
2. Controller receives the request
3. Controller passes the query string to the service
4. Service checks:
   - is the query null?
   - is it blank?
   - should we trim spaces?
5. Service calls repository search method
6. Repository runs query against Player table
7. Matching players are returned
8. Controller sends results back as JSON

PlayerController.java handles the web request
Ex:

- receives /api/players/search?query=steph

PlayerService.java handles application logic
Ex:

- trims extra spaces
- checks if the query is blank
- decides what to return

PlayerRepository.java handles database access
Ex:

- search full_name, first_name, and last_name

Repository classes are where data access and search behavior belong.
"A mechanism for encapsulating storage, retrieval, and search behavior which emulates a collection of objects"

If the user searches: Steph

The repository checks whether:

- fullName contains "steph"
- or firstName contains "steph"
- or lastName contains "steph"

so "stephen curry" should match

PlayerService.java file sits between the controller and the repository
It's job is to hold app logic that is not specifically HTTP handling and not specifically database access.
Spring's @Service javadoc describes it as a business service facade

Repostiory Bean: an object that provides data access logic and is managed by the Spring Inversion Of Control Container

Without PlayerService.java, the controller might start doing things like:

- trimming input
- checking for blank input
- deciding what to return
- later mapping entities to DTOs

That makes the controller too busy
Having PlayerService.java keeps responsibilites sepearated

PlayerController = HTTP
Service = app logic
Repository = database

# @RequestMapping

You can use @RequestMapping annotation to map requests to controllers methods

1. Browser sends GET /api/players/search?query=steph
2. PlayerController receives the request
3. PlayerController sends "steph" to PlayerService
4. PlayerService checks if it is blank and trims it
5. PlayerService calls PlayerRepository
6. PlayerRepository runs the database search
7. Matching players come back
8. Spring returns them as JSON

The controller broke because I had one field for repository and one field for service
"I know how to set playerRepository but waht about playerService? It is final so I need it initialized too"

Fixed endpoints
@RequestMapping("/api/players")
plus @GetMapping ("/api/players")

became api/players/api/players
I removed the unnecessary endpoint for getAllPlayers

We can now ask the API directly rather than query from postgres

## Use the browser/Postman when you want to test:

“Does my endpoint work?”
“Does my controller/service/repository chain work?”
“What JSON comes back?”

## Use PostgreSQL terminal when you want to test:

“Is the data actually in the table?”
“Did my script insert what I expected?”
“Is this a backend bug or a database/data bug?”

## Next Endpoint

Fetch one player by id

## GET /api/players/1

search for a player -> click a player -> load that player's details

## Why this Works

`findById(id)`
We don't have to write this yourself in the repository because it already comes from JpaRepository.
Spring Data JPA's repository model provides common data access methods and lets you add custom ones when needed.

It Returns Optional<Player>
Spring uses Optional for cases where a row may or may not exist.

That means:

- Maybe a player with id 1 exists
- Maybe a player with id 999999 does not

Why we use orElseThrow(...)
Because we want a clear failure instead of returning null silently.

## Why @PathVariable is used here

The id is part of the URL path itself
`/api/players/1`

not a query
`/api/players/search?query=lebron`

- @RequestParam -> reads from ?query=...
- @PathVariable -> reads from /players/{id}

# Player season stats

`GET /api/players/{id}/season-stats`

## Example

`GET /api/players/1/season-stats`

Find the player with databse id 1, then return that player's season stats rows

PlayerSeasonStat.java tells spring:

- what table to use
- what columns exist
- what Java fields map to those columns

Database Table:
player_season_stats
PlayerSeasonStat.java

## Why findByPlayerId works?

Because PlayerSeasonStat has this field:
`private Player player;`
Spring understands:
`findByPlayerId(Long playerId)`

"Find all PlayerSeasonStat rows where player.id = ?"

Player Season stats table was empty
Need to ingest player season stats and build the table

```
GET /api/players/1/season-stats
    ->
Spring Boot endpoint works
    ->
Repository query works
    ->
Database returns no matching rows
    ->
[]
```

## Ingest season stats from Python into PostgreSQL

### High Level overview of Python Ingestion

```
1. Fetch season stats data from nba_api
2. Transform the response into my database shape
3. Match each stat row to my interal players.id
4. Insert rows into player_season_stats
```

## Key understanding

player_season_stats table does not use nba_player_id directly as its foreign key
it uses player_id points to players.id

## Ingestion Logic

1. Look up the player in players table using nba_player_id
2. Get the row's internal id
3. Insert season stats using that internal player_id

## Inspect player rows

```
nba_player_insight=# SELECT id, nba_player_id, full_name
nba_player_insight-# FROM players
nba_player_insight-# ORDER BY id
nba_player_insight-# LIMIT 10;
 id | nba_player_id |      full_name
----+---------------+---------------------
  1 |         76001 | Alaa Abdelnaby
  2 |         76002 | Zaid Abdul-Aziz
  3 |         76003 | Kareem Abdul-Jabbar
  4 |            51 | Mahmoud Abdul-Rauf
  5 |          1505 | Tariq Abdul-Wahad
  6 |           949 | Shareef Abdur-Rahim
  7 |         76005 | Tom Abernethy
  8 |         76006 | Forest Able
  9 |         76007 | John Abramovic
 10 |        203518 | Alex Abrines
```

## Player season stats table structure

```
nba_player_insight=# \d player_season_stats
                                            Table "public.player_season_stats"
      Column       |            Type             | Collation | Nullable |                     Default
-------------------+-----------------------------+-----------+----------+-------------------------------------------------
 id                | bigint                      |           | not null | nextval('player_season_stats_id_seq'::regclass)
 player_id         | bigint                      |           | not null |
 season            | character varying(20)       |           | not null |
 games_played      | integer                     |           |          |
 minutes_per_game  | numeric(5,2)                |           |          |
 points_per_game   | numeric(5,2)                |           |          |
 rebounds_per_game | numeric(5,2)                |           |          |
 assists_per_game  | numeric(5,2)                |           |          |
 steals_per_game   | numeric(5,2)                |           |          |
 blocks_per_game   | numeric(5,2)                |           |          |
 field_goal_pct    | numeric(5,3)                |           |          |
 three_point_pct   | numeric(5,3)                |           |          |
 free_throw_pct    | numeric(5,3)                |           |          |
 created_at        | timestamp without time zone |           |          | CURRENT_TIMESTAMP
:
```

## Python script Psuedocode

```
1. Connect to the database
2. Query the players table and build a dictionary:
   nba_player_id -> internal player id

3. Fetch season stats from nba_api
4. Loop through each season stats row
5. Read the nba player id from that row
6. Check if that nba player id exists in our local players table
   - if not, skip it

7. Convert the season stats row into our database columns
8. Insert into player_season_stats using internal player_id
9. Use ON CONFLICT (player_id, season) to update existing rows or skip duplicates
10. Commit the transaction
11. Print how many rows were inserted/updated/skipped
```

The NBA api gives us PLAYER_ID but our table needs player_id, so we build a Python dictionary that translates between them

## Why ON CONFLCIT matters

Our table relies on a unique rule (player_id, season)
If we rerun the script for the smae season we do not want duplicate rows

```
ON CONFLICT (player_id, season)
DO UPDATE SET ...
```

means: if that player-season already exists, update the stats instead of failing

### How to Run

From the project root

```
source venv/bin/activate
python scripts/load_player_season_stats.py
```

How to verify it worked
After the script runs, go into Postgres:

```
psql -d nba_player_insight
```

Then run:

```
SELECT COUNT(*) FROM player_season_stats;
```

Test a few rows:

```
SELECT *
FROM player_season_stats
LIMIT 10;
```

Test the join:

```
SELECT p.full_name, pss.season, pss.points_per_game
FROM player_season_stats pss
JOIN players p ON pss.player_id = p.id
LIMIT 10;
```

Test the API:
`http://localhost:8080/api/players/1/season-stats`

# What I accomplished

- Build the backend endpoint to fetch a player's season stats by internal player id.
- When the endpoint returned an empty result, I investigated and found that the player_season_stats table had no data
- To Fix that I created a new Python ETL script to load 2024-25 regular season player season stats from nba_api into PostgreSQL
- The script first builds a lookup dictionary from players.nba_player_id to players.id so we can map external NBA player IDs to our internal database IDs efficiently.
- Then it transforms the API response into our database schema and inserts the rows into player_season_stats using ON CONFLICT so the script is safe to rerun

# feature/player-game-logs-endpoint

1. Design the new tables
2. Write the ingestion script
3. Load the data
4. Verify the SQL rows look correct
5. Build the Spring endpoint

We can use two data tables

- games -> the game played
- player_game_logs -> an individual player's performance for a game

```
A game exists on its own
A player game log is a player's stat line for the one game
Many players can belong to a single game M -> 1
One player can have many game logs across many games 1 -> M

teams <- games
teams <- players
players <- player game logs -> games
```

## games should not have player_id as its foreign key

Because a game exists even before we focus on any single player

- Lakers vs Warrios
- On a certain date
- On a certain season
- with a certain NBA game id

one game --> many player_game_logs
one player --> many player_game logs

## Games:

One row = one nba game
Primary Key:
id

Unique NBA field:
nba_game_id

Foreign Keys:
home_team_id
away_team_id

Player_game_logs
One row = one player's stat line in one NBA game

Primary key:
id

Foreign Keys:
player_id
game_id
(and maybe team_id)

Unique constraint:
(player_id, game_id)

# April 21st 2026:

1. Understand the API response shape
2. Understand the app's needs
3. Design a schema that works well for your app
4. Transform the API data into that schema

Transformation step is the important part

Design your database around your app's needs
While making sure the API can realistically provide the data required

```
The API gives me raw data in its own structure
My databse does not need to copy that structure exactly
I should design my schema around what my app needs
as long as the API gives me enough information to transform the data into that schema
```

Goal of the Script

```
1. Call nba_api to get player game log data
2. For each API row, find the matching player in our players table
3. For each API row, find the matching team in our teams table
4. Create or update the corresponding game row in games
5. Create or update the corresponding player game log row in player_game_logs
```

## Key Learning Idea before coding

Script needs to Translate

### External NBA ids

from the API:

- nba_player_id
- nba_team_id
- nba_game_id

Into the app's

### Internal DB ids

- players.id
- teams.id
- games.id

The API gives us external NBA ids.
Our databse tables relate to each other using internal databse ids.
So we need a lookup to translate external ids into internal ids before inserting rows.
A dictionary is a good choice because the lookup is fast and easy to reuse inside the loop.

1. Read the NBA ids from the API row
2. Translate nba_player_id into players.id
3. Translate nba_team_id into teams.id
4. Insert or update the game row first because player_game_logs depends on a valid game row existing
5. Then get games.id because player_game_logs.game_id must reference the internal games.id
6. Then insert or update player_game_logs because we now have all the internal foreign keys needed for that row

# April 24th 2026:

The getter method name, return type, and field must all match.
If the field is private Game game -> Getter: public Game getGame() returning game
The pattern is always consistent

A JPA Entity is a file that maps our database's columns and data tables to be able to be used in Java through getters and setters.
The entity is Java's representation of our data table

I need to mark the primary key with @id
I need to provide my foreign key relationships liek @ManyToOne or @OneToOne
I need to mark foreign keys with @JoinColumn -> maps a relationship field to a foreign key column
@Column maps a regular field to a column

I need to have my objects so that we can make sure of the entire Object's fields and methods

The entity allows us to layout the framework of how are database is structure and create a foundation for Java to be able to use all of the columns

## Sharpened version

A JPA entity is a Java class that maps to a database table
Each instance of the class represents one row
We need four things:

- @Entity to tell Spring this class maps to a table
- @Table to specify which table
- @Id to mark the primary key
- @Column/JoinColumn for each field

Regular columns use @Column
Foreign key relationships use @ManyToOne and @JoinColumn
So JPA fetches the whole related object instaed of just an id

Repository only deals with each specific object. Jpa handles the joins internally

# April 26th 2026:

I built:

- PlayerGameStat.java
- Game.java
- Team.java
- PlayerGameStatRepository.java
- PlayerService.java
- PlayerController.java

"What are the four files I need to create to add a new endpoint in Spring Boot, and what does each one do"

1. I need to have a SQL file with an existing data table which I can grab data from
2. I need to have an entity file which maps the database data table to Java
3. I need a repository file to help us fetch the data from the database
4. I need a service file which has the method that the endpoint is trying to accomplish
5. I need a controller which calls the call method on the backend

## Refined Version

# How to add a new endpoint in Spring Boot

1. SQL Table: the actual data structure in PostgreSQL that everything maps to
2. Entity: The java class that represents one row from that table, with fields for each column
3. Repository: the interface that talks to the databse, Spring generates the SQL automatically from method names
4. Service: holds the business logic, decides what to do with the data before returning it
5. Controller: handles the HTTP request, calls the service, returns the response as JSON

"Why do we need to to insert the game row before the player_game_log row?"
Because the player_game_logs game_id column references games id so we need to insert the parent row before the child row can reference it.

## Referential Integrity

The database enforces that a child row cannot reference a parent that doesn't exist yet.

"Build Team Lookup dictionary, nba_team_id -> teams.id"
Why do you need a team lookup?
What are you going to use for it when you insert rows

player_game_logs has a team_id column we want to be able to quickly translate the nba_team_id into our internal data's column of team_id.

flow for each API row:
read nba_game_id from API row
-> insert that game into games table (or skip if it already exists)
-> ask the database "what is the internal id for that nba_game_id?"
-> Use that internal id when inserting into player_game_logs

The player lookup and team lookup need to be built before we start looping through the API row.

Because for every single row in the loop we need to translate the external IDs to the internal IDs instantly

Building the dictionary once upfront is much more efficent than going through the database every time

1. Build player lookup dictionary
2. Build team lookup dictionary
3. Fetch game log rows from the API
4. Loop through each row - inside the loop you insert games and player_game_logs

Games don't need a lookup dictionary because you create them inside the loop and immediately fetch back the id that was just generated.

We query our own database for active players and build a dictionary translating nba_player_id to our internal db_player_id.
Then the outer loop goes through each active player, calls the NBA API to get that specific player's game log for that season and converts the response into a dataframe so we can loop through each game row in the inner loop

## What is the difference between a JOIN and a LEFT JOIN, and when would I use one?

- A JOIN: is an inner join and it is strict it only returns rows where both sides have matching value
  "Give me gmaes that have a matching team row" If home_team_id is NULL, there's no match - that game row gets thrown out entirely

- A LEFT JOIN: "give meall games and if there's a matching team row incude it, but if there isn't that's fine, just return NULL for those team columns."


