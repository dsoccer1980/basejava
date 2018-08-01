CREATE TABLE resume
(
  uuid      CHAR(36) PRIMARY KEY NOT NULL,
  full_name TEXT                 NOT NULL
);

CREATE TABLE contact
(
  id          SERIAL PRIMARY KEY NOT NULL,
  resume_uuid CHAR(36)           NOT NULL REFERENCES resume ON DELETE CASCADE,
  type        TEXT               NOT NULL,
  value       TEXT               NOT NULL
);

CREATE UNIQUE INDEX contact_uuid_type_index
  ON contact (resume_uuid, type);



