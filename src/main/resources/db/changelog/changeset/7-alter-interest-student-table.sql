ALTER TABLE users
    ADD COLUMN describe_id BIGINT,
    ADD constraint fk_user_describe FOREIGN KEY (describe_id) REFERENCES describes(id);
