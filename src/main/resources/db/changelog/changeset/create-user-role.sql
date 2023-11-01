CREATE TABLE roles (
       id                  BIGINT NOT NULL PRIMARY KEY,
       name                VARCHAR(50) NOT NULL
);

CREATE TABLE users (
    id                  BIGINT NOT NULL PRIMARY KEY,
    created_date        DATE,
    updated_date        DATE,
    active              BOOLEAN NOT NULL DEFAULT FALSE,
    firstname           VARCHAR(50),
    lastname            VARCHAR(50),
    email               VARCHAR(100),
    password            VARCHAR(255),
    role_id             BIGINT,
    CONSTRAINT fk_user_roles FOREIGN KEY (role_id) REFERENCES roles(id)
);
