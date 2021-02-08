CREATE TABLE city
(
    id     SERIAL PRIMARY KEY,
    name   TEXT NOT NULL UNIQUE,
    info   TEXT
)