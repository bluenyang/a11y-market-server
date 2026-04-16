-- 1. addresses
ALTER TABLE addresses
    DROP CONSTRAINT fk_address_user,
    ADD CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE;

-- 2. user_oauth_links
ALTER TABLE user_oauth_links
    DROP CONSTRAINT fk_user_oauth_links_user,
    ADD CONSTRAINT fk_user_oauth_links_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE;

-- 3. default_addresses
ALTER TABLE default_addresses
    DROP CONSTRAINT fk_default_users,
    ADD CONSTRAINT fk_default_users FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE;

-- 4. sellers
ALTER TABLE sellers
    DROP CONSTRAINT fk_seller_user,
    ADD CONSTRAINT fk_seller_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE;

-- 5. products (NULL 허용 처리와 제약조건 변경을 한 번에!)
ALTER TABLE products
    ALTER COLUMN seller_id DROP NOT NULL,
    DROP CONSTRAINT fk_seller,
    ADD CONSTRAINT fk_seller FOREIGN KEY (seller_id) REFERENCES sellers (seller_id) ON DELETE SET NULL;

-- 6. carts
ALTER TABLE carts
    DROP CONSTRAINT fk_cart_user,
    ADD CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE;

-- 7. cart_items (🔥 두 개의 제약조건 변경을 한 큐에 묶어서 처리)
ALTER TABLE cart_items
    DROP CONSTRAINT fk_cart_item_product,
    ADD CONSTRAINT fk_cart_item_product FOREIGN KEY (product_id) REFERENCES products (product_id) ON DELETE CASCADE,
    DROP CONSTRAINT fk_cart_item_cart,
    ADD CONSTRAINT fk_cart_item_cart FOREIGN KEY (cart_id) REFERENCES carts (cart_id) ON DELETE CASCADE;

-- 8. refresh_token (Postgres 타입으로 변환)
DROP TABLE IF EXISTS refresh_token; -- IF EXISTS를 붙여주면 에러 없이 깔끔합니다.
CREATE TABLE refresh_token
(
    refresh_token_id uuid PRIMARY KEY,          -- RAW(16) -> uuid
    user_id          uuid      NOT NULL UNIQUE, -- RAW(16) -> uuid
    token            text      NOT NULL,        -- VARCHAR2(512) -> text (Postgres 권장 방식)
    expiry_date      timestamp NOT NULL,        -- TIMESTAMP 유지
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE CASCADE
);

-- 9. user_a11y_profiles
ALTER TABLE user_a11y_profiles
    DROP CONSTRAINT fk_user_a11y_profiles,
    ADD CONSTRAINT fk_user_a11y_profiles FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE;

-- 10. product_ai_summary
DELETE
FROM product_ai_summary;
ALTER TABLE product_ai_summary
    DROP CONSTRAINT fk_product_ai_summary_product,
    ADD CONSTRAINT fk_product_ai_summary_product FOREIGN KEY (product_id) REFERENCES products (product_id) ON DELETE CASCADE;

-- 11. product_images
ALTER TABLE product_images
    DROP CONSTRAINT fk_product,
    ADD CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products (product_id) ON DELETE CASCADE;