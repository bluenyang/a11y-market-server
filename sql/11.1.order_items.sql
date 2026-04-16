CREATE TABLE order_items
(
    order_item_id     UUID PRIMARY KEY,
    order_id          UUID         NOT NULL,
    product_id        UUID         NOT NULL,
    product_name      VARCHAR(255) NOT NULL,
    product_price     INT          NOT NULL,
    product_quantity  INT          NOT NULL,
    order_item_status VARCHAR(20)  NOT NULL,
    cancel_reason     TEXT,
    CONSTRAINT fk_orderitem_order FOREIGN KEY (order_id)
        REFERENCES orders (order_id),
    CONSTRAINT fk_orderitem_product FOREIGN KEY (product_id)
        REFERENCES products (product_id)
);