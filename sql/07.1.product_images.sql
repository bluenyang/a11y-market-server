CREATE TABLE product_images (
   image_id   RAW(16) PRIMARY KEY,
   product_id RAW(16) NOT NULL,
   image_url  VARCHAR2(2048) NOT NULL,
   alt_text   CLOB,
   created_at TIMESTAMP DEFAULT current_timestamp,
   CONSTRAINT fk_product FOREIGN KEY ( product_id )
      REFERENCES products ( product_id )
);