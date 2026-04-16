CREATE TABLE user_a11y_settings
(
    user_id            UUID PRIMARY KEY,
    contrast_level     BOOLEAN     DEFAULT FALSE             NOT NULL,
    text_size_level    BOOLEAN     DEFAULT FALSE             NOT NULL,
    text_spacing_level BOOLEAN     DEFAULT FALSE             NOT NULL,
    line_height_level  BOOLEAN     DEFAULT FALSE             NOT NULL,
    text_align         VARCHAR(10) DEFAULT 'left'            NOT NULL,
    screen_reader      BOOLEAN     DEFAULT FALSE             NOT NULL,
    smart_contrast     BOOLEAN     DEFAULT FALSE             NOT NULL,
    highlight_links    BOOLEAN     DEFAULT FALSE             NOT NULL,
    cursor_highlight   BOOLEAN     DEFAULT FALSE             NOT NULL,
    updated_at         TIMESTAMP   DEFAULT current_timestamp NOT NULL,
    CONSTRAINT fk_user_a11y_settings FOREIGN KEY (user_id)
        REFERENCES users (user_id)
        ON DELETE CASCADE
);