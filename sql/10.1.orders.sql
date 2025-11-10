CREATE TABLE orders (
   order_id         RAW(16) PRIMARY KEY,
   user_name        VARCHAR2(30) NOT NULL,
   user_email       VARCHAR2(150) NOT NULL,
   user_phone       VARCHAR2(15) NOT NULL,
   receiver_name    VARCHAR2(30) NOT NULL,
   receiver_phone   VARCHAR2(15) NOT NULL,
   receiver_zipcode CHAR(5) NOT NULL,
   receiver_addr1   VARCHAR2(100) NOT NULL,
   receiver_addr2   VARCHAR2(200),
   total_price      INT NOT NULL,
   order_status     VARCHAR2(30) NOT NULL,
   created_at       TIMESTAMP DEFAULT current_timestamp,
   CONSTRAINT chk_user_email_or_phone
      CHECK ( user_email IS NOT NULL
          OR user_phone IS NOT NULL )
);