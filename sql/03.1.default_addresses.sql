CREATE TABLE default_addresses (
   user_id    RAW(16) PRIMARY KEY NOT NULL,
   address_id RAW(16) NOT NULL,
   CONSTRAINT fk_default_users FOREIGN KEY ( user_id )
      REFERENCES users ( user_id ),
   CONSTRAINT fk_default_addresses FOREIGN KEY ( address_id )
      REFERENCES addresses ( address_id )
);