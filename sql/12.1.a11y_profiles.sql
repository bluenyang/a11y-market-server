CREATE TABLE a11y_profiles
(
    profile_id     UUID PRIMARY KEY,
    user_id        UUID        NOT NULL,
    profile_name   VARCHAR(80) NOT NULL,
    font_size      VARCHAR(15) NOT NULL,
    contrast       VARCHAR(15) NOT NULL,
    text_to_speech INT         NOT NULL,
    updated_at     TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT fk_a11y_profiles_users FOREIGN KEY (user_id)
        REFERENCES users (user_id)
);