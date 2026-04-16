ALTER TABLE user_oauth_links
    ALTER COLUMN user_id TYPE uuid USING user_id::uuid,
    ALTER COLUMN user_id DROP NOT NULL;