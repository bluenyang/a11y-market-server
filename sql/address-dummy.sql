INSERT INTO address (
   address_id,
   user_id,
   receiver_name,
   receiver_phone,
   receiver_zipcode,
   receiver_addr1,
   receiver_addr2,
   created_at
)
SELECT 'ad000000-0000-0000-0000-000000000001',
       'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
       '김관리',
       '010-0000-0001',
       04524,
       '서울특별시 중구 관리로 1',
       '101동 1001호',
       SYSTIMESTAMP FROM dual
UNION ALL
SELECT 'ad000000-0000-0000-0000-000000000002',
       'b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a12',
       '박판매',
       '010-1111-2222',
       13456,
       '경기도 성남시 판교로 55',
       '203동 503호',
       SYSTIMESTAMP FROM dual
UNION ALL
SELECT 'ad000000-0000-0000-0000-000000000003',
       'b2eebc99-9c0b-4ef8-bb6d-6bb9bd380a13',
       '이판매',
       '010-3333-4444',
       14312,
       '경기도 용인시 판매로 7',
       '502동 1203호',
       SYSTIMESTAMP FROM dual
UNION ALL
SELECT 'ad000000-0000-0000-0000-000000000004',
       'b3eebc99-9c0b-4ef8-bb6d-6bb9bd380a14',
       '오판매',
       '010-5555-6666',
       22344,
       '부산광역시 해운대로 101',
       'A동 904호',
       SYSTIMESTAMP FROM dual
UNION ALL
SELECT 'ad000000-0000-0000-0000-000000000005',
       'c1eebc99-9c0b-4ef8-bb6d-6bb9bd380a15',
       '최사용',
       '010-1234-5678',
       01234,
       '서울특별시 강남구 사용로 55',
       '101호',
       SYSTIMESTAMP FROM dual
UNION ALL
SELECT 'ad000000-0000-0000-0000-000000000006',
       'c2eebc99-9c0b-4ef8-bb6d-6bb9bd380a16',
       '정사용',
       '010-9876-5432',
       21211,
       '인천광역시 서구 정로 23',
       NULL,
       SYSTIMESTAMP FROM dual
UNION ALL
SELECT 'ad000000-0000-0000-0000-000000000007',
       'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380a17',
       '강사용',
       '010-1111-9999',
       33222,
       '대전광역시 강구로 99',
       '302호',
       SYSTIMESTAMP FROM dual
UNION ALL
SELECT 'ad000000-0000-0000-0000-000000000008',
       'c4eebc99-9c0b-4ef8-bb6d-6bb9bd380a18',
       '조사용',
       '010-4444-3333',
       44333,
       '광주광역시 조구로 14',
       'B동 502호',
       SYSTIMESTAMP FROM dual
UNION ALL
SELECT 'ad000000-0000-0000-0000-000000000009',
       'c5eebc99-9c0b-4ef8-bb6d-6bb9bd380a19',
       '윤사용',
       '010-7777-8888',
       55444,
       '대구광역시 윤구로 17',
       '201호',
       SYSTIMESTAMP FROM dual
UNION ALL
SELECT 'ad000000-0000-0000-0000-000000000010',
       'c6eebc99-9c0b-4ef8-bb6d-6bb9bd380a20',
       '임사용',
       '010-6666-5555',
       66555,
       '울산광역시 임로 33',
       '601호',
       SYSTIMESTAMP FROM dual;

COMMIT;

SELECT * FROM address;

delete from address;
