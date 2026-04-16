CREATE TABLE product_images
(
    image_id   UUID PRIMARY KEY,
    product_id UUID          NOT NULL,
    image_url  VARCHAR(2048) NOT NULL,
    alt_text   TEXT,
    created_at TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT fk_product FOREIGN KEY (product_id)
        REFERENCES products (product_id)
);