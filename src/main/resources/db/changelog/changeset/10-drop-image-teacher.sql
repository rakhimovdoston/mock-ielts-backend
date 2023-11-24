ALTER TABLE images
    DROP CONSTRAINT fk_teacher_images;

ALTER TABLE images
    DROP COLUMN teacher_id;