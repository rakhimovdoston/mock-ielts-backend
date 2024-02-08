CREATE TABLE user_tokens (
         id                           SERIAL PRIMARY KEY,
         created_date                 TIMESTAMP,
         updated_date                 TIMESTAMP,
         active                       BOOLEAN NOT NULL DEFAULT FALSE,
         access_token                 TEXT,
         refresh_token                VARCHAR(255),
         expire_date                  TIMESTAMP,
         refresh_expire_date          TIMESTAMP,
         user_id                      BIGINT,
         FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
);