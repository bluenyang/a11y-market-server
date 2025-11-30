CREATE TABLE products (
   product_id          RAW(16) PRIMARY KEY,
   seller_id           RAW(16) NOT NULL,
   category_id         RAW(16) NOT NULL,
   product_price       INT NOT NULL,
   product_stock       INT NOT NULL,
   product_name        VARCHAR2(256) NOT NULL,
   product_description CLOB NOT NULL,
   product_ai_summary  CLOB,
   product_status      VARCHAR2(20) NOT NULL,
   submit_date         TIMESTAMP DEFAULT current_timestamp NOT NULL,
   approved_date       TIMESTAMP,
   updated_at          TIMESTAMP DEFAULT current_timestamp NOT NULL,
   CONSTRAINT fk_seller FOREIGN KEY ( seller_id )
      REFERENCES sellers ( seller_id ),
   CONSTRAINT fk_category FOREIGN KEY ( category_id )
      REFERENCES categories ( category_id )
);