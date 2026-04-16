CREATE TABLE products
(
    product_id          UUID PRIMARY KEY,
    seller_id           UUID                                NOT NULL,
    category_id         UUID                                NOT NULL,
    product_price       INT                                 NOT NULL,
    product_stock       INT                                 NOT NULL,
    product_name        VARCHAR(256)                        NOT NULL,
    product_description TEXT                                NOT NULL,
    product_ai_summary  TEXT,
    product_status      VARCHAR(20)                         NOT NULL,
    submit_date         TIMESTAMP DEFAULT current_timestamp NOT NULL,
    approved_date       TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT current_timestamp NOT NULL,
    CONSTRAINT fk_seller FOREIGN KEY (seller_id)
        REFERENCES sellers (seller_id),
    CONSTRAINT fk_category FOREIGN KEY (category_id)
        REFERENCES categories (category_id)
);