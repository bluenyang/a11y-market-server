INSERT INTO sellers (
   seller_id,
   user_id,
   seller_name,
   business_number,
   seller_grade,
   seller_intro,
   is_a11y_guarantee,
   seller_submit_status,
   submit_date,
   approved_date,
   updated_at
)
   SELECT hextoraw('019a69f1f60c73ab8d829233d471597d'),
          hextoraw('019a698a43ea778587a64ba7e9e58784'),
          '강철 상점',
          '111-11-11111',
          'BRONZE',
          '강철처럼 튼튼한 제품을 판매합니다.',
          1,
          'APPROVED',
          current_timestamp,
          current_timestamp,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a698a43ea7dca8b34cde9a3850adb'),
          '친절한 영희네',
          '222-22-22222',
          'SILVER',
          '고객님께 항상 친절한 상점입니다.',
          0,
          'APPROVED',
          current_timestamp,
          current_timestamp,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f1f60c76ea9cbdafb048644daf'),
          hextoraw('019a698a43ea7ba38634d17b296bd88c'),
          '산소탱크 스포츠',
          '333-33-33333',
          'BRONZE',
          '지치지 않는 열정으로 스포츠 용품을 판매합니다.',
          0,
          'APPROVED',
          current_timestamp,
          current_timestamp,
          current_timestamp
     FROM dual;

COMMIT;