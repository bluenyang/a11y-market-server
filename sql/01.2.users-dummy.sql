INSERT INTO users (
   user_id,
   user_name,
   user_pass,
   user_email,
   user_phone,
   user_nickname,
   user_role,
   created_at,
   updated_at
)
   SELECT hextoraw('019a698a43ea778587a64ba7e9e58784'),
          '김철수',
          '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
          'user1@example.com',
          '01012345671',
          '강철개발자',
          'USER',
          current_timestamp,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698a43ea7dca8b34cde9a3850adb'),
          '이영희',
          '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
          'user2@example.com',
          '01012345672',
          '친절한영희씨',
          'USER',
          current_timestamp,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698a43ea7ba38634d17b296bd88c'),
          '박지성',
          '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
          'user3@example.com',
          '01012345673',
          '산소탱크',
          'USER',
          current_timestamp,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698a43ea74eb8e3451462055755e'),
          '관리자',
          '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
          'admin@example.com',
          '01098765432',
          '총관리자',
          'ADMIN',
          current_timestamp,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698a43ea784389bd8d1b6e9a5cfe'),
          '최민식',
          '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
          'user4@example.com',
          '01012345674',
          '연기파배우',
          'USER',
          current_timestamp,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698a43ea7f6db7d6b0682abbd378'),
          '유재석',
          '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
          'user5@example.com',
          '01012345675',
          '메뚜기',
          'USER',
          current_timestamp,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698a43ea75a7ae59bcec4f0361ac'),
          '아이유',
          '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
          'user6@example.com',
          '01012345676',
          '이지금',
          'USER',
          current_timestamp,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698a43ea7e4aa01065cd58d2ccd8'),
          '손흥민',
          '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
          'user7@example.com',
          '01012345677',
          '캡틴손',
          'USER',
          current_timestamp,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698a43ea7b279103a70f0065184a'),
          '김연아',
          '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
          'user8@example.com',
          '01012345678',
          '피겨퀸',
          'USER',
          current_timestamp,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698a43ea7c958b8ae2ed1625f1f8'),
          '이순신',
          '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
          'user9@example.com',
          '01012345679',
          '성웅',
          'USER',
          current_timestamp,
          current_timestamp
     FROM dual;

COMMIT;