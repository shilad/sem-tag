CREATE INDEX IF NOT EXISTS tagapps_idx_tag_app_id ON tagapps(tag_app_id);
CREATE INDEX IF NOT EXISTS tagapps_idx_user_id ON tagapps(user_id);
CREATE INDEX IF NOT EXISTS tagapps_idx_norm_tag ON tagapps(norm_tag);
CREATE INDEX IF NOT EXISTS tagapps_idx_item_id ON tagapps(item_id);
CREATE INDEX IF NOT EXISTS tagapps_idx_concept_id ON tagapps(concept_id);
