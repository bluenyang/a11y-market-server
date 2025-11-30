CREATE TABLE sellers (
   seller_id            RAW(16) PRIMARY KEY,
   user_id              RAW(16) NOT NULL,
   seller_name          VARCHAR2(255) NOT NULL,
   business_number      VARCHAR2(50) NOT NULL,
   seller_grade         VARCHAR2(30) NOT NULL,
   seller_intro         VARCHAR2(1024),
   is_a11y_guarantee    NUMBER(1) DEFAULT 0 NOT NULL,
   seller_submit_status VARCHAR2(20) NOT NULL,
   submit_date          TIMESTAMP NOT NULL,
   approved_date        TIMESTAMP,
   updated_at           TIMESTAMP NOT NULL,
   CONSTRAINT fk_seller_user FOREIGN KEY ( user_id )
      REFERENCES users ( user_id )
);