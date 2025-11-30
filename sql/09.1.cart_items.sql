CREATE TABLE cart_items (
   cart_item_id RAW(16) PRIMARY KEY,
   product_id   RAW(16) NOT NULL,
   cart_id      RAW(16) NOT NULL,
   quantity     INT NOT NULL,
   CONSTRAINT fk_cart_item_product FOREIGN KEY ( product_id )
      REFERENCES products ( product_id ),
   CONSTRAINT fk_cart_item_cart FOREIGN KEY ( cart_id )
      REFERENCES carts ( cart_id )
);