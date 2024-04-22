create table courses
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    active       BOOLEAN NOT NULL DEFAULT FALSE,
    img          VARCHAR(255),
    title        VARCHAR(255),
    description  TEXT,
    price        DECIMAL,
    discount     DECIMAL,
    start_date   TIMESTAMP,
    available    BOOLEAN          DEFAULT FALSE,
    category_id  BIGINT,
    teacher_id   BIGINT,
    CONSTRAINT fk_courses_categories FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_courses_teacher FOREIGN KEY (teacher_id) REFERENCES teachers (id)
);

create table lessons
(
    id           SERIAL PRIMARY KEY,
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    active       BOOLEAN NOT NULL DEFAULT FALSE,
    title        VARCHAR(255),
    description  TEXT,
    image        VARCHAR(255),
    course_id    BIGINT,
    CONSTRAINT fk_lesson_courses FOREIGN KEY (course_id) REFERENCES courses (id)
);