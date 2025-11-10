CREATE TABLE addresses (
   address_id       RAW(16) PRIMARY KEY,
   user_id          RAW(16) NOT NULL,
   address_name     VARCHAR2(100) NOT NULL,
   receiver_name    VARCHAR2(30) NOT NULL,
   receiver_phone   VARCHAR2(15) NOT NULL,
   receiver_zipcode INT NOT NULL,
   receiver_addr1   VARCHAR2(100) NOT NULL,
   receiver_addr2   VARCHAR2(200),
   created_at       TIMESTAMP NOT NULL,
   CONSTRAINT fk_address_user FOREIGN KEY ( user_id )
      REFERENCES users ( user_id )
);