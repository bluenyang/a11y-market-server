CREATE TABLE user_oauth_links (
   user_oauth_link_id RAW(16) PRIMARY KEY,
   user_id            RAW(16) NOT NULL,
   oauth_provider     VARCHAR2(50) NOT NULL,
   oauth_provider_id  VARCHAR2(255) NOT NULL,
   created_at         TIMESTAMP DEFAULT current_timestamp,
   CONSTRAINT fk_user_oauth_links_user FOREIGN KEY ( user_id )
      REFERENCES users ( user_id )
);