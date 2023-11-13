CREATE TABLE teachers(
     id                  SERIAL PRIMARY KEY,
     created_date        DATE,
     updated_date        DATE,
     active              BOOLEAN NOT NULL DEFAULT FALSE,
     firstname           VARCHAR(50),
     lastname            VARCHAR(50),
     email               VARCHAR(100),
     phone_number        VARCHAR(20),
     description         TEXT,
     topics              jsonb,
     certificates        jsonb,
     user_id             BIGINT,
     CONSTRAINT fk_user_teacher FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE experiences (
     id                  SERIAL PRIMARY KEY,
     created_date        DATE,
     updated_date        DATE,
     active              BOOLEAN NOT NULL DEFAULT FALSE,
     title               VARCHAR(150),
     company_name        VARCHAR(100),
     entry               VARCHAR(50),
     ends                 VARCHAR(50),
     description         TEXT,
     teacher_id          BIGINT,
     CONSTRAINT fk_teacher_experiences FOREIGN KEY (teacher_id) REFERENCES teachers(id)
);

CREATE TABLE educations (
    id                  SERIAL PRIMARY KEY,
    created_date        DATE,
    updated_date        DATE,
    active              BOOLEAN NOT NULL DEFAULT FALSE,
    name                VARCHAR(100),
    url                 VARCHAR(255),
    faculty             VARCHAR(100),
    degree              VARCHAR(25),
    teacher_id          BIGINT,
    CONSTRAINT fk_teacher_educations FOREIGN KEY (teacher_id) REFERENCES teachers(id)
);

CREATE TABLE images (
    id                  SERIAL PRIMARY KEY,
    created_date        DATE,
    updated_date        DATE,
    active              BOOLEAN NOT NULL DEFAULT FALSE,
    content_type        VARCHAR(50),
    filename            VARCHAR(150),
    teacher_id          BIGINT,
    CONSTRAINT fk_teacher_images FOREIGN KEY (teacher_id) REFERENCES teachers(id)
);