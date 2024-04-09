create table question_history
(
    id            SERIAL PRIMARY KEY,
    created_date  DATE,
    updated_date  DATE,
    active        BOOLEAN NOT NULL DEFAULT FALSE,
    request       jsonb,
    date          DATE,
    correct_count integer          DEFAULT 0,
    user_id       BIGINT,
    CONSTRAINT fk_question_history_user FOREIGN KEY (user_id) REFERENCES users (id)
)