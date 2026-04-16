CREATE TABLE refresh_token
(
    refresh_token_id UUID PRIMARY KEY,
    user_id          UUID         NOT NULL UNIQUE,
    token            VARCHAR(512) NOT NULL,
    expiry_date      TIMESTAMP    NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);