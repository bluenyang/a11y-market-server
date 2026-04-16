CREATE TABLE carts
(
    cart_id UUID PRIMARY KEY NOT NULL,
    user_id UUID             NOT NULL,
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id)
        REFERENCES users (user_id)
);