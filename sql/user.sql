CREATE TABLE users (
   user_id       VARCHAR2(36) PRIMARY KEY,
   user_name     VARCHAR2(30) NOT NULL,
   user_pass     VARCHAR2(100) NOT NULL,
   user_email    VARCHAR2(50) UNIQUE NOT NULL,
   user_phone    VARCHAR2(15) NOT NULL,
   user_nickname VARCHAR2(30),
   user_role     VARCHAR2(30) NOT NULL,
   created_at    TIMESTAMP DEFAULT current_timestamp,
   updated_at    TIMESTAMP DEFAULT current_timestamp
);

CREATE OR REPLACE TRIGGER trg_update_timestamp BEFORE
   UPDATE ON users
   FOR EACH ROW
BEGIN
   :new.updated_at := current_timestamp;
END;

DROP TABLE users;