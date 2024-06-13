CREATE TABLE organization
(
    id                   BIGSERIAL PRIMARY KEY,
    name                 VARCHAR(255) NOT NULL UNIQUE,
    description          TEXT,
    registration_number  VARCHAR(255),
    address              VARCHAR(255),
    city                 VARCHAR(255),
    phone_number         VARCHAR(255),
    email                VARCHAR(255),
    website              VARCHAR(255),
    contact_person       VARCHAR(255),
    contact_person_phone VARCHAR(255),
    contact_person_email VARCHAR(255),
    established_date     DATE,
    owner_id             BIGINT,
    FOREIGN KEY (owner_id) REFERENCES users (id)
);

ALTER TABLE images
    add column organization_id BIGINT,
    add constraint fk_organisation_images FOREIGN KEY (organization_id) REFERENCES images (id);