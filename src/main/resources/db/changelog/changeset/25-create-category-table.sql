create table categories
(
    id          SERIAL PRIMARY KEY,
    name        varchar(255),
    description TEXT,
    image       varchar(255)
);