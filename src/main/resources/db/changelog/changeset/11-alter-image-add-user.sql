ALTER TABLE images
    ADD COLUMN user_id BIGINT,
    ADD CONSTRAINT fk_teacher_images FOREIGN KEY (user_id) REFERENCES users(id);