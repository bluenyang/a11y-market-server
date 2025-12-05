CREATE TABLE main_page_events (
   event_id          RAW(16) PRIMARY KEY,
   event_title       VARCHAR2(200) NOT NULL,
   event_description VARCHAR2(1000) NOT NULL,
   event_image_url   VARCHAR2(2048) NOT NULL,
   event_url         VARCHAR2(2048),
   start_date        DATE NOT NULL,
   end_date          DATE NOT NULL
);

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