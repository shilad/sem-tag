CREATE TABLE IF NOT EXISTS tagapp (
  tag_app_id IDENTITY PRIMARY KEY NOT NULL,
  user_id VARCHAR NOT NULL,
  tag VARCHAR NOT NULL,
  item_id VARCHAR NOT NULL,
  timestamp TIMESTAMP NOT NULL
);