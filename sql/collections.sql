-- 1. 사용자 테이블 (Users)
CREATE TABLE users (
   user_id       RAW(16) PRIMARY KEY,
   user_name     VARCHAR2(30) NOT NULL,
   user_pass     VARCHAR2(60),
   user_email    VARCHAR2(254) UNIQUE,
   user_phone    VARCHAR2(15) UNIQUE,
   user_nickname VARCHAR2(100),
   user_role     VARCHAR2(30) NOT NULL,
   created_at    TIMESTAMP DEFAULT current_timestamp,
   updated_at    TIMESTAMP DEFAULT current_timestamp,
   CONSTRAINT chk_user_contact
      CHECK ( user_email IS NOT NULL
          OR user_phone IS NOT NULL )
);

-- 2. 소셜 로그인 연동 (User OAuth Links)
CREATE TABLE user_oauth_links (
   user_oauth_link_id RAW(16) PRIMARY KEY,
   user_id            RAW(16) NOT NULL,
   oauth_provider     VARCHAR2(50) NOT NULL,
   oauth_provider_id  VARCHAR2(255) NOT NULL,
   created_at         TIMESTAMP DEFAULT current_timestamp,
   CONSTRAINT fk_user_oauth_links_user FOREIGN KEY ( user_id )
      REFERENCES users ( user_id )
);

-- 3. 인증 리프레시 토큰 (Refresh Token)
CREATE TABLE refresh_token (
   refresh_token_id RAW(16) PRIMARY KEY,
   user_id          RAW(16) NOT NULL UNIQUE,
   token            VARCHAR2(512) NOT NULL,
   expiry_date      TIMESTAMP NOT NULL,
   CONSTRAINT fk_refresh_token_user FOREIGN KEY ( user_id )
      REFERENCES users ( user_id )
         ON DELETE CASCADE
);

-- 4. 사용자 배송지 (Addresses)
CREATE TABLE addresses (
   address_id       RAW(16) PRIMARY KEY,
   user_id          RAW(16) NOT NULL,
   address_name     VARCHAR2(100) NOT NULL,
   receiver_name    VARCHAR2(30) NOT NULL,
   receiver_phone   VARCHAR2(15) NOT NULL,
   receiver_zipcode CHAR(5) NOT NULL,
   receiver_addr1   VARCHAR2(300) NOT NULL,
   receiver_addr2   VARCHAR2(300),
   created_at       TIMESTAMP NOT NULL,
   is_default       NUMBER(1,0) DEFAULT 0 NOT NULL,
   CONSTRAINT fk_address_user FOREIGN KEY ( user_id )
      REFERENCES users ( user_id )
);

-- 5. 사용자 접근성 프로필 (User A11y Profiles)
CREATE TABLE user_a11y_profiles (
   profile_id         RAW(16) PRIMARY KEY,
   user_id            RAW(16) NOT NULL,
   profile_name       VARCHAR2(50) NOT NULL,
   description        VARCHAR2(200),
   is_preset          NUMBER(1,0) DEFAULT 0 NOT NULL,
   contrast_level     NUMBER(1,0) NOT NULL,
   text_size_level    NUMBER(1,0) NOT NULL,
   text_spacing_level NUMBER(1,0) NOT NULL,
   line_height_level  NUMBER(1,0) NOT NULL,
   text_align         VARCHAR2(10) NOT NULL,
   screen_reader      NUMBER(1,0) NOT NULL,
   smart_contrast     NUMBER(1,0) NOT NULL,
   highlight_links    NUMBER(1,0) NOT NULL,
   cursor_highlight   NUMBER(1,0) NOT NULL,
   created_at         TIMESTAMP DEFAULT systimestamp,
   updated_at         TIMESTAMP DEFAULT systimestamp,
   CONSTRAINT uk_user_profile_name UNIQUE ( user_id,
                                            profile_name )
);

-- 6. 판매자 정보 (Sellers)
CREATE TABLE sellers (
   seller_id            RAW(16) PRIMARY KEY,
   user_id              RAW(16) NOT NULL,
   seller_name          VARCHAR2(255) NOT NULL,
   business_number      VARCHAR2(50) NOT NULL,
   seller_grade         VARCHAR2(30) NOT NULL,
   seller_intro         VARCHAR2(1024),
   is_a11y_guarantee    NUMBER(1) DEFAULT 0 NOT NULL,
   seller_submit_status VARCHAR2(20) NOT NULL,
   submit_date          TIMESTAMP NOT NULL,
   approved_date        TIMESTAMP,
   updated_at           TIMESTAMP NOT NULL,
   CONSTRAINT fk_seller_user FOREIGN KEY ( user_id )
      REFERENCES users ( user_id )
);

-- 7. 카테고리 (Categories)
CREATE TABLE categories (
   category_id   RAW(16) PRIMARY KEY,
   parent_cat_id RAW(16),
   category_name VARCHAR2(100) NOT NULL,
   CONSTRAINT fk_parent_cat FOREIGN KEY ( parent_cat_id )
      REFERENCES categories ( category_id )
);

-- 8. 상품 (Products)
CREATE TABLE products (
   product_id          RAW(16) PRIMARY KEY,
   seller_id           RAW(16) NOT NULL,
   category_id         RAW(16) NOT NULL,
   product_price       INT NOT NULL,
   product_stock       INT NOT NULL,
   product_name        VARCHAR2(256) NOT NULL,
   product_description CLOB NOT NULL,
   product_ai_summary  CLOB,
   product_status      VARCHAR2(20) NOT NULL,
   submit_date         TIMESTAMP DEFAULT current_timestamp NOT NULL,
   approved_date       TIMESTAMP,
   updated_at          TIMESTAMP DEFAULT current_timestamp NOT NULL,
   CONSTRAINT fk_seller FOREIGN KEY ( seller_id )
      REFERENCES sellers ( seller_id ),
   CONSTRAINT fk_category FOREIGN KEY ( category_id )
      REFERENCES categories ( category_id )
);

-- 9. 상품 AI 요약 상세 (Product AI Summary)
CREATE TABLE product_ai_summary (
   product_id    RAW(16) PRIMARY KEY,
   summary_text  CLOB,
   usage_context CLOB,
   usage_method  CLOB,
   generated_at  TIMESTAMP DEFAULT current_timestamp
);

-- 10. 상품 이미지 (Product Images)
-- image_sequence 컬럼 및 인덱스 포함
CREATE TABLE product_images (
   image_id       RAW(16) PRIMARY KEY,
   product_id     RAW(16) NOT NULL,
   image_url      VARCHAR2(2048) NOT NULL,
   alt_text       CLOB,
   created_at     TIMESTAMP DEFAULT current_timestamp,
   image_sequence INT NOT NULL,
   CONSTRAINT fk_product FOREIGN KEY ( product_id )
      REFERENCES products ( product_id )
);

-- 이미지 시퀀스에 대한 고유 인덱스
CREATE UNIQUE INDEX idx_product_image_sequence ON
   product_images (
      product_id,
      image_sequence
   );

-- 11. 장바구니 (Carts)
CREATE TABLE carts (
   cart_id RAW(16) PRIMARY KEY NOT NULL,
   user_id RAW(16) NOT NULL,
   CONSTRAINT fk_cart_user FOREIGN KEY ( user_id )
      REFERENCES users ( user_id )
);

-- 12. 장바구니 상세 아이템 (Cart Items)
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

-- 13. 주문 (Orders)
CREATE TABLE orders (
   order_id         RAW(16) PRIMARY KEY,
   user_name        VARCHAR2(30) NOT NULL,
   user_email       VARCHAR2(150) NOT NULL,
   user_phone       VARCHAR2(15) NOT NULL,
   receiver_name    VARCHAR2(30) NOT NULL,
   receiver_phone   VARCHAR2(15) NOT NULL,
   receiver_zipcode CHAR(5) NOT NULL,
   receiver_addr1   VARCHAR2(300) NOT NULL,
   receiver_addr2   VARCHAR2(300),
   total_price      INT NOT NULL,
   order_status     VARCHAR2(30) NOT NULL,
   created_at       TIMESTAMP DEFAULT current_timestamp,
   payment_key      VARCHAR2(200),
   CONSTRAINT chk_user_email_or_phone
      CHECK ( user_email IS NOT NULL
          OR user_phone IS NOT NULL )
);

-- 14. 주문 상세 아이템 (Order Items)
CREATE TABLE order_items (
   order_item_id     RAW(16) PRIMARY KEY,
   order_id          RAW(16) NOT NULL,
   product_id        RAW(16) NOT NULL,
   product_name      VARCHAR2(255) NOT NULL,
   product_price     INT NOT NULL,
   product_quantity  INT NOT NULL,
   order_item_status VARCHAR2(20) NOT NULL,
   cancel_reason     CLOB,
   product_image_url VARCHAR2(2048),
   CONSTRAINT fk_orderitem_order FOREIGN KEY ( order_id )
      REFERENCES orders ( order_id ),
   CONSTRAINT fk_orderitem_product FOREIGN KEY ( product_id )
      REFERENCES products ( product_id )
);

-- 15. 메인 페이지 이벤트 배너 (Main Page Events)
CREATE TABLE main_page_events (
   event_id          RAW(16) PRIMARY KEY,
   event_title       VARCHAR2(200) NOT NULL,
   event_description VARCHAR2(1000) NOT NULL,
   event_image_url   VARCHAR2(2048) NOT NULL,
   event_url         VARCHAR2(2048),
   start_date        DATE NOT NULL,
   end_date          DATE NOT NULL
);

-- 초기 배너 데이터
INSERT INTO main_page_events (
   event_id,
   event_title,
   event_description,
   event_image_url,
   event_url,
   start_date,
   end_date
)
   SELECT hextoraw('019aecba29aa7166bdf1c571a79f81ca'),
          '신규 회원 환영 이벤트',
          '새로 가입한 회원들을 위한 특별 환영 이벤트! 다양한 혜택과 쿠폰을 드립니다.',
          '/a11ymarket-bucket/images/events/welcome_event_banner.png',
          '/events/welcome',
          TO_DATE('2024-01-01','YYYY-MM-DD'),
          TO_DATE('2099-12-31','YYYY-MM-DD')
     FROM dual
   UNION ALL
   SELECT hextoraw('019aecbd61b07f7fac9895c64c991f81'),
          '무료 배송 프로모션',
          '모든 주문에 대해 무료 배송 혜택을 제공합니다. 지금 바로 쇼핑하세요!',
          '/a11ymarket-bucket/images/events/free_shipping_banner.png',
          '/events/free-shipping',
          TO_DATE('2025-12-01','YYYY-MM-DD'),
          TO_DATE('2026-06-30','YYYY-MM-DD')
     FROM dual
   UNION ALL
   SELECT hextoraw('019aecbe3edc73f1a3789347e6422e79'),
          '신년 대비 건강 캠페인',
          '새해를 맞이하여 건강한 시작을 위한 특별 캠페인을 진행합니다. 건강 관련 상품들을 특별 할인된 가격에 만나보세요!',
          '/a11ymarket-bucket/images/events/health_campaign_banner.png',
          '/products?category=019a69f3-b7b4-7253-8447-2516faee63f4',
          TO_DATE('2025-12-01','YYYY-MM-DD'),
          TO_DATE('2026-01-31','YYYY-MM-DD')
     FROM dual;
COMMIT;

-- 16. 판매자 대시보드 통계 뷰 (Seller Dashboard Stats View)
CREATE OR REPLACE VIEW view_seller_dashboard_stats AS
   SELECT s.seller_id,
          SUM(
             CASE
                WHEN oi.order_item_status = 'CONFIRMED' THEN
                   oi.product_price * oi.product_quantity
                ELSE
                   0
             END
          ) AS total_revenue,
          COUNT(DISTINCT
             CASE
                WHEN oi.order_item_status != 'REJECTED' THEN
                   oi.order_item_id
             END
          ) AS total_order_count,
          COUNT(
             CASE
                WHEN oi.order_item_status = 'CONFIRMED' THEN
                   1
             END
          ) AS confirmed_count,
          COUNT(
             CASE
                WHEN oi.order_item_status IN('CANCELED',
                                             'RETURNED') THEN
                   1
             END
          ) AS refunded_count
     FROM sellers s
     LEFT JOIN products p
   ON s.seller_id = p.seller_id
     LEFT JOIN order_items oi
   ON p.product_id = oi.product_id
    WHERE p.product_status = 'APPROVED'
    GROUP BY s.seller_id;

-- 17. 판매자 인기 상품 뷰 (Seller Top Products View)
CREATE OR REPLACE VIEW view_seller_top_products AS
   SELECT p.seller_id,
          p.product_id,
          p.product_name,
          p.product_price,
          pi.image_url AS product_image_url,
          COUNT(oi.order_item_id) AS order_count,
          SUM(oi.product_quantity) AS total_quantity_sold,
          SUM(oi.product_price * oi.product_quantity) AS total_sales_amount,
          RANK()
          OVER(PARTITION BY p.seller_id
               ORDER BY SUM(oi.product_price * oi.product_quantity) DESC
          ) AS sales_rank
     FROM products p
     LEFT JOIN product_images pi
   ON p.product_id = pi.product_id
     LEFT JOIN order_items oi
   ON p.product_id = oi.product_id
      AND oi.order_item_status = 'CONFIRMED'
    WHERE p.product_status = 'APPROVED'
    GROUP BY p.seller_id,
             p.product_id,
             p.product_name,
             p.product_price,
             pi.image_url
   HAVING COUNT(oi.order_item_id) > 0
    ORDER BY p.seller_id,
             sales_rank;

-- 18. 월간 인기 상품 뷰 (Monthly Popular Products View)
CREATE OR REPLACE VIEW view_monthly_popular_products AS
   SELECT p.product_id,
          p.product_name,
          p.product_price,
          (
             SELECT image_url
               FROM product_images pi
              WHERE pi.product_id = p.product_id
                AND pi.image_sequence = 0
          ) AS product_image_url,
          p.category_id,
          cat.category_name,
          p.seller_id,
          SUM(oi.product_quantity) AS monthly_sales_volume,
          COUNT(DISTINCT o.order_id) AS monthly_order_count,
          RANK()
          OVER(
              ORDER BY SUM(oi.product_quantity) DESC
          ) AS ranking
     FROM products p
     JOIN order_items oi
   ON p.product_id = oi.product_id
     JOIN orders o
   ON oi.order_id = o.order_id
     JOIN categories cat
   ON p.category_id = cat.category_id
    WHERE o.created_at >= add_months(
         sysdate,
         -1
      )
      AND o.created_at < sysdate
      AND oi.order_item_status IN ( 'PAID',
                                    'ACCEPTED',
                                    'SHIPPED',
                                    'CONFIRMED' )
      AND p.product_status = 'APPROVED'
    GROUP BY p.product_id,
             p.product_name,
             p.product_price,
             p.category_id,
             cat.category_name,
             p.seller_id
    ORDER BY monthly_sales_volume DESC;

-- 19. 카테고리 추천 뷰 (Category Recommendations View)
CREATE OR REPLACE VIEW view_category_recommendations AS
   WITH category_tree (
      root_id,
      root_name,
      leaf_id
   ) AS (
      SELECT category_id,
             category_name,
             category_id
        FROM categories
       WHERE parent_cat_id IS NULL
      UNION ALL
      SELECT p.root_id,
             p.root_name,
             cat.category_id
        FROM categories cat
        JOIN category_tree p
      ON cat.parent_cat_id = p.leaf_id
   ),ranked_products AS (
      SELECT ct.root_id,
             ct.root_name,
             vp.product_id,
             vp.product_name,
             vp.product_price,
             vp.product_image_url,
             vp.monthly_sales_volume,
             ROW_NUMBER()
             OVER(PARTITION BY ct.root_id
                  ORDER BY vp.monthly_sales_volume DESC,
                           vp.product_id DESC
             ) AS rn
        FROM view_monthly_popular_products vp
        JOIN category_tree ct
      ON vp.category_id = ct.leaf_id
   )
   SELECT *
     FROM ranked_products
    WHERE rn <= 4;