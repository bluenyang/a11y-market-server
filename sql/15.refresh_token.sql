CREATE TABLE refresh_token
(
    refresh_token_id RAW(16) PRIMARY KEY,
    user_id          RAW(16)       NOT NULL UNIQUE,
    token            VARCHAR2(512) NOT NULL,
    expiry_date      TIMESTAMP     NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);