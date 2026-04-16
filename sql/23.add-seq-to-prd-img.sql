ALTER TABLE product_images
    ADD COLUMN image_sequence INT DEFAULT 1 NOT NULL;

CREATE UNIQUE INDEX idx_product_image_sequence ON
    product_images (
                    product_id,
                    image_sequence
        );