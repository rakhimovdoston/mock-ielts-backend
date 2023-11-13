CREATE TABLE describes(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(100) NOT NULL
);

CREATE TABLE goals (
    id              SERIAL PRIMARY KEY,
    name                VARCHAR(100) NOT NULL
);

CREATE TABLE topics (
   id              SERIAL PRIMARY KEY,
   name            VARCHAR(100) NOT NULL
);
