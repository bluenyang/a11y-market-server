INSERT INTO orders (
   order_id,
   user_name,
   user_email,
   user_phone,
   receiver_name,
   receiver_phone,
   receiver_zipcode,
   receiver_addr1,
   receiver_addr2,
   total_price,
   order_status,
   created_at
)
   SELECT hextoraw('019a69ef120970ef8f929678c20cf1f0'),
          '김철수',
          'user1@example.com',
          '01012345671',
          '김철수',
          '010-1234-5671',
          '12345',
          '서울시 강남구 테헤란로 1',
          '101동 101호',
          35000,
          'PAID',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69ef120972d2a5eb3782f9d74394'),
          '아이유',
          'user6@example.com',
          '01012345676',
          '아이유 (작업실)',
          '010-6666-6666',
          '98765',
          '서울시 성동구 성수동',
          '스튜디오 301호',
          120000,
          'PAID',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69ef120970008ca16118c0da4ef1'),
          '이순신',
          'user9@example.com',
          '01012345679',
          '이순신 (서울숙소)',
          '010-0000-0000',
          '56565',
          '서울시 동작구 노량진동',
          '오피스텔',
          89000,
          'PAID',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69ef12097ef5ab7423c9c6db1b3a'),
          '박지성',
          'user3@example.com',
          '01012345673',
          '박지성 (사무실)',
          '010-9876-5432',
          '45678',
          '서울시 중구 세종대로 4',
          nvl(
             NULL,
             ' '
          ),
          5000,
          'PAID',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69ef12097c25bf970cd1a4297387'),
          '이영희',
          'user2@example.com',
          '01012345672',
          '이영희 (사무실)',
          '010-3333-3333',
          '65432',
          '서울시 서초구 서초대로 300',
          '사무실 5층',
          42000,
          'PAID',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69ef12097a44a10534f077af76b8'),
          '손흥민',
          'user7@example.com',
          '01012345677',
          '손흥민',
          '010-1234-5677',
          '89012',
          '강원도 춘천시 8',
          '808동 808호',
          76500,
          'PAID_PENDING',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69ef120976b4be402c2049a835f3'),
          '김철수',
          'user1@example.com',
          '01012345671',
          '김철수 (회사)',
          '010-1111-1111',
          '54321',
          '서울시 강남구 테헤란로 200',
          '강남파이낸스센터',
          15000,
          'PAID',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69ef120978f6bd6bc652586fb59d'),
          '아이유',
          'user6@example.com',
          '01012345676',
          '이지은 (실명배송)',
          '010-8888-8888',
          '34343',
          '제주도 제주시 애월읍',
          '별장',
          230000,
          'PAID',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69ef1209708a824e0e46e9608bfa'),
          '박지성',
          'user3@example.com',
          '01012345673',
          '박지성',
          '010-1234-5673',
          '34567',
          '경기도 수원시 영통구 3',
          '303동 303호',
          55000,
          'PAID',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69ef120973cd855c4ea2bc0bb5f6'),
          '이순신',
          'user9@example.com',
          '01012345679',
          '이순신 (거제도)',
          '010-9999-9999',
          '45454',
          '경상남도 거제시',
          '조선소 근처',
          19000,
          'PAID',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69ef12097c13808bd2aeb55c0007'),
          '최민식',
          'user4@example.com',
          '01012345674',
          '최민식',
          '010-1234-5674',
          '56789',
          '부산시 해운대구 5',
          '505동 505호',
          41000,
          'PAID',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69ef12097b18be006047fdb2b94c'),
          '김연아',
          'user8@example.com',
          '01012345678',
          '김연아',
          '010-1234-5678',
          '90123',
          '경기도 군포시 9',
          '909동 909호',
          99000,
          'PAID_PENDING',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69ef1209739481ee148dc3539d56'),
          '박지성',
          'user3@example.com',
          '01012345673',
          '박지성 (본사)',
          '010-5555-5555',
          '87654',
          '서울시 종로구 세종대로 100',
          '광화문빌딩',
          28000,
          'PAID',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69ef12097d3a8bbdfa45b0c9f568'),
          '유재석',
          'user5@example.com',
          '01012345675',
          '유재석',
          '010-1234-5675',
          '67890',
          '서울시 마포구 상암동 6',
          '606동 606호',
          33000,
          'PAID',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69ef12097b71bbcd641190f9341e'),
          '아이유',
          'user6@example.com',
          '01012345676',
          '아이유',
          '010-1234-5676',
          '78901',
          '서울시 강남구 청담동 7',
          '707동 707호',
          52000,
          'PAID',
          current_timestamp
     FROM dual;

COMMIT;