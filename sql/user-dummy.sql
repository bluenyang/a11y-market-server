INSERT INTO users (
   user_id,
   user_name,
   user_pass,
   user_email,
   user_phone,
   user_nickname,
   user_role
)
   SELECT 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
          '김관리',
          '$2a$10$fA.5.Tj9.1c.hC8.iB7qG.H0L/1Qe.Z.oY.x/1bK.zJ.p',
          'admin@example.com',
          '010-0000-0001',
          '총관리자',
          'ROLE_ADMIN'
     FROM dual
   UNION ALL
   SELECT 'b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a12',
          '박판매',
          '$2a$10$fA.5.Tj9.1c.hC8.iB7qG.H0L/1Qe.Z.oY.x/1bK.zJ.p',
          'seller1@example.com',
          '010-1111-2222',
          '판매자박',
          'ROLE_SELLER'
     FROM dual
   UNION ALL
   SELECT 'b2eebc99-9c0b-4ef8-bb6d-6bb9bd380a13',
          '이판매',
          '$2a$10$fA.5.Tj9.1c.hC8.iB7qG.H0L/1Qe.Z.oY.x/1bK.zJ.p',
          'seller2@example.com',
          '010-3333-4444',
          NULL,
          'ROLE_SELLER'
     FROM dual
   UNION ALL
   SELECT 'b3eebc99-9c0b-4ef8-bb6d-6bb9bd380a14',
          '오판매',
          '$2a$10$fA.5.Tj9.1c.hC8.iB7qG.H0L/1Qe.Z.oY.x/1bK.zJ.p',
          'seller3@example.com',
          '010-5555-6666',
          '친절한오씨',
          'ROLE_SELLER'
     FROM dual
   UNION ALL
   SELECT 'c1eebc99-9c0b-4ef8-bb6d-6bb9bd380a15',
          '최사용',
          '$2a$10$fA.5.Tj9.1c.hC8.iB7qG.H0L/1Qe.Z.oY.x/1bK.zJ.p',
          'user1@example.com',
          '010-1234-5678',
          '유저최',
          'ROLE_USER'
     FROM dual
   UNION ALL
   SELECT 'c2eebc99-9c0b-4ef8-bb6d-6bb9bd380a16',
          '정사용',
          '$2a$10$fA.5.Tj9.1c.hC8.iB7qG.H0L/1Qe.Z.oY.x/1bK.zJ.p',
          'user2@example.com',
          '010-9876-5432',
          NULL,
          'ROLE_USER'
     FROM dual
   UNION ALL
   SELECT 'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380a17',
          '강사용',
          '$2a$10$fA.5.Tj9.1c.hC8.iB7qG.H0L/1Qe.Z.oY.x/1bK.zJ.p',
          'user3@example.com',
          '010-1111-9999',
          '강강',
          'ROLE_USER'
     FROM dual
   UNION ALL
   SELECT 'c4eebc99-9c0b-4ef8-bb6d-6bb9bd380a18',
          '조사용',
          '$2a$10$fA.5.Tj9.1c.hC8.iB7qG.H0L/1Qe.Z.oY.x/1bK.zJ.p',
          'user4@example.com',
          '010-4444-3333',
          '조조',
          'ROLE_USER'
     FROM dual
   UNION ALL
   SELECT 'c5eebc99-9c0b-4ef8-bb6d-6bb9bd380a19',
          '윤사용',
          '$2a$10$fA.5.Tj9.1c.hC8.iB7qG.H0L/1Qe.Z.oY.x/1bK.zJ.p',
          'user5@example.com',
          '010-7777-8888',
          NULL,
          'ROLE_USER'
     FROM dual
   UNION ALL
   SELECT 'c6eebc99-9c0b-4ef8-bb6d-6bb9bd380a20',
          '임사용',
          '$2a$10$fA.5.Tj9.1c.hC8.iB7qG.H0L/1Qe.Z.oY.x/1bK.zJ.p',
          'user6@example.com',
          '010-6666-5555',
          '사용자임',
          'ROLE_USER'
     FROM dual;

COMMIT;

SELECT *
  FROM users;

DELETE FROM users;
COMMIT;