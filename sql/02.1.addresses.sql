CREATE TABLE addresses (
   address_id       RAW(16) PRIMARY KEY,
   user_id          RAW(16) NOT NULL,
   address_name     VARCHAR2(100) NOT NULL,
   receiver_name    VARCHAR2(30) NOT NULL,
   receiver_phone   VARCHAR2(15) NOT NULL,
   receiver_zipcode CHAR(5) NOT NULL,
   receiver_addr1   VARCHAR2(100) NOT NULL,
   receiver_addr2   VARCHAR2(200),
   created_at       TIMESTAMP NOT NULL,
   CONSTRAINT fk_address_user FOREIGN KEY ( user_id )
      REFERENCES users ( user_id )
);

-- alter int to char(5) for receiver_zipcode
-- temp column -> copy data -> drop old column -> rename temp column
-- ALTER TABLE addresses ADD (receiver_zipcode_tmp CHAR(5));
-- UPDATE addresses SET receiver_zipcode_tmp = lpad(to_char(receiver_zipcode), 5, '0');
-- ALTER TABLE addresses DROP COLUMN receiver_zipcode;
-- ALTER TABLE addresses RENAME COLUMN receiver_zipcode_tmp TO receiver_zipcode;