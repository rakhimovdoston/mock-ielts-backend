create table questions
(
    id           SERIAL PRIMARY KEY,
    created_date DATE,
    updated_date DATE,
    active       BOOLEAN NOT NULL DEFAULT FALSE,
    name         TEXT,
    user_id      BIGINT  NOT NULL
);

create table answers
(
    id           SERIAL PRIMARY KEY,
    created_date DATE,
    updated_date DATE,
    active       BOOLEAN NOT NULL DEFAULT FALSE,
    name         TEXT,
    correct      BOOLEAN NOT NULL DEFAULT FALSE,
    question_id  BIGINT,
    CONSTRAINT fk_question_answers FOREIGN KEY (question_id) REFERENCES questions (id)
);