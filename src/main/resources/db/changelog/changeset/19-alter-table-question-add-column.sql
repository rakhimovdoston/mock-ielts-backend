alter table questions
    add column difficulty  varchar(50),
    add column category_id BIGINT,
    add constraint fk_question_category FOREIGN KEY (category_id) REFERENCES question_categories(id);