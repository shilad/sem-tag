CREATE TABLE IF NOT EXISTS tagapps (
  tag_app_id IDENTITY PRIMARY KEY NOT NULL,
  user_id VARCHAR(256) NOT NULL,
  raw_tag VARCHAR(256) NOT NULL,
  norm_tag VARCHAR(256) NOT NULL,
  item_id VARCHAR(256) NOT NULL,
  timestamp TIMESTAMP NOT NULL,
  concept_id BIGINT NOT NULL
);