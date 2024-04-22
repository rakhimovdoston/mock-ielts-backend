alter table teachers
    drop column certificates;

create table certificates
(
    id               SERIAL PRIMARY KEY,
    created_date     TIMESTAMP,
    updated_date     TIMESTAMP,
    active           BOOLEAN NOT NULL DEFAULT FALSE,
    reading          DOUBLE PRECISION,
    listening        DOUBLE PRECISION,
    writing          DOUBLE PRECISION,
    speaking         DOUBLE PRECISION,
    overall          DOUBLE PRECISION,
    url              VARCHAR(255),
    certificate_type VARCHAR(100),
    test_type        VARCHAR(100),
    teacher_id       BIGINT,
    image_id            BIGINT,
    CONSTRAINT fk_certificate_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id),
    CONSTRAINT fk_certificate_images FOREIGN KEY (image_id) REFERENCES images(id)
);