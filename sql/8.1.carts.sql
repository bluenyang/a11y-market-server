CREATE TABLE carts (
   cart_id RAW(16) PRIMARY KEY NOT NULL,
   user_id RAW(16) NOT NULL,
   CONSTRAINT fk_cart_user FOREIGN KEY ( user_id )
      REFERENCES users ( user_id )
);