CREATE TABLE chat_rooms
(
    id               SERIAL PRIMARY KEY,
    created_date     TIMESTAMP,
    updated_date     TIMESTAMP,
    active           BOOLEAN NOT NULL DEFAULT FALSE,
    chat_id          VARCHAR(100),
    receiver_user_id BIGINT,
    sender_user_id   BIGINT,
    CONSTRAINT fk_receiver_user FOREIGN KEY (receiver_user_id) REFERENCES users (id),
    CONSTRAINT fk_courses_teacher FOREIGN KEY (sender_user_id) REFERENCES users (id)
);

CREATE TABLE chat_messages
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    active       BOOLEAN NOT NULL DEFAULT FALSE,
    chat_id      VARCHAR(100),
    content      TEXT,
    timestamp    TIMESTAMP,
    message_type    VARCHAR(50),
    file_url        VARCHAR(255),
    from_user_id    BIGINT
)