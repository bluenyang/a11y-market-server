-- order_items의 product_id 컬럼을 NULL 허용으로 변경
ALTER TABLE order_items
    ALTER COLUMN product_id DROP NOT NULL,
    DROP CONSTRAINT fk_orderitem_product,
    ADD CONSTRAINT fk_orderitem_product
        FOREIGN KEY (product_id)
            REFERENCES products (product_id)
            ON DELETE SET NULL;