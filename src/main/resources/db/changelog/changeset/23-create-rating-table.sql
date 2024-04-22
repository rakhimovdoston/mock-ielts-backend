CREATE TABLE ratings
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    active       BOOLEAN NOT NULL DEFAULT FALSE,
    rating       decimal NOT NULL DEFAULT 0,
    user_id      BIGINT,
    teacher_id   BIGINT,
    constraint fk_teacher_ratings FOREIGN KEY (teacher_id) REFERENCES teachers(id),
    constraint fk_user_ratings FOREIGN KEY (user_id) REFERENCES users(id)
);
