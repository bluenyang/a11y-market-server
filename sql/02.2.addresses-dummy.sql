INSERT INTO addresses (
   address_id,
   user_id,
   address_name,
   receiver_name,
   receiver_phone,
   receiver_zipcode,
   receiver_addr1,
   receiver_addr2,
   created_at
)
   SELECT hextoraw('019a698d82c07c66ac4a293c84acfa52'),
          hextoraw('019a698a43ea778587a64ba7e9e58784'),
          '집',
          '김철수',
          '010-1234-5671',
          '12345',
          '서울시 강남구 테헤란로 1',
          '101동 101호',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698d82c07499b33d51a99e798e68'),
          hextoraw('019a698a43ea7dca8b34cde9a3850adb'),
          '집',
          '이영희',
          '010-1234-5672',
          '23456',
          '서울시 서초구 반포대로 2',
          '202동 202호',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698d82c079b1907acd71118370aa'),
          hextoraw('019a698a43ea7ba38634d17b296bd88c'),
          '집',
          '박지성',
          '010-1234-5673',
          '34567',
          '경기도 수원시 영통구 3',
          '303동 303호',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698d82c076f9a7d0bf5219a3dd1e'),
          hextoraw('019a698a43ea7ba38634d17b296bd88c'),
          '박지성 사무실',
          '박지성 (사무실)',
          '010-9876-5432',
          '45678',
          '서울시 중구 세종대로 4',
          NULL,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698d82c070e99f2a30bbf500e1ab'),
          hextoraw('019a698a43ea784389bd8d1b6e9a5cfe'),
          '집',
          '최민식',
          '010-1234-5674',
          '56789',
          '부산시 해운대구 5',
          '505동 505호',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698d82c077d5aa4f70751fece6d7'),
          hextoraw('019a698a43ea7f6db7d6b0682abbd378'),
          '집',
          '유재석',
          '010-1234-5675',
          '67890',
          '서울시 마포구 상암동 6',
          '606동 606호',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698d82c07d549e272b0e0ae113ab'),
          hextoraw('019a698a43ea75a7ae59bcec4f0361ac'),
          '집',
          '아이유',
          '010-1234-5676',
          '78901',
          '서울시 강남구 청담동 7',
          '707동 707호',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698d82c07ab0b452942f32cfa5dc'),
          hextoraw('019a698a43ea7e4aa01065cd58d2ccd8'),
          '집',
          '손흥민',
          '010-1234-5677',
          '89012',
          '강원도 춘천시 8',
          '808동 808호',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698d82c07e6bb1a316575c02472a'),
          hextoraw('019a698a43ea7b279103a70f0065184a'),
          '집',
          '김연아',
          '010-1234-5678',
          '90123',
          '경기도 군포시 9',
          '909동 909호',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698d82c07ebc8d299ee1144b2660'),
          hextoraw('019a698a43ea7c958b8ae2ed1625f1f8'),
          '집',
          '이순신',
          '010-1234-5679',
          '11234',
          '전라남도 여수시 10',
          '1010동 1010호',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a699408e07ef094f297c04602190b'),
          hextoraw('019a698a43ea778587a64ba7e9e58784'),
          '회사',
          '김철수 (회사)',
          '010-1111-1111',
          '54321',
          '서울시 강남구 테헤란로 200',
          '강남파이낸스센터',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a699408e07d028ff13b65c56ff860'),
          hextoraw('019a698a43ea778587a64ba7e9e58784'),
          '본가',
          '김철수 (본가)',
          '010-2222-2222',
          '54322',
          '경기도 성남시 분당구',
          '101동 202호',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a699408e072f9bd401915f7ce6cb4'),
          hextoraw('019a698a43ea7dca8b34cde9a3850adb'),
          '사무실',
          '이영희 (사무실)',
          '010-3333-3333',
          '65432',
          '서울시 서초구 서초대로 300',
          '사무실 5층',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a699408e07101987672d4f407929b'),
          hextoraw('019a698a43ea7f6db7d6b0682abbd378'),
          '유재석 물류센터',
          '유재석 (물류센터)',
          '010-4444-4444',
          '76543',
          '경기도 이천시 물류단지',
          'A동 101',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a699408e072008e5e5648644fbac7'),
          hextoraw('019a698a43ea7ba38634d17b296bd88c'),
          '박지성 본사',
          '박지성 (본사)',
          '010-5555-5555',
          '87654',
          '서울시 종로구 세종대로 100',
          '광화문빌딩',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a699408e075b8bf43b2def45efab4'),
          hextoraw('019a698a43ea75a7ae59bcec4f0361ac'),
          '작업실',
          '아이유 (작업실)',
          '010-6666-6666',
          '98765',
          '서울시 성동구 성수동',
          '스튜디오 301호',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a699408e076258607c2e61dc20319'),
          hextoraw('019a698a43ea75a7ae59bcec4f0361ac'),
          '본가',
          '아이유 (본가)',
          '010-7777-7777',
          '12121',
          '경기도 과천시',
          '주택',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a699408e07444bedcc3cb3f43ecdf'),
          hextoraw('019a698a43ea75a7ae59bcec4f0361ac'),
          '제주도 별장',
          '이지은 (실명배송)',
          '010-8888-8888',
          '34343',
          '제주도 제주시 애월읍',
          '별장',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a699408e07d14b14bbb68e835a6f1'),
          hextoraw('019a698a43ea7c958b8ae2ed1625f1f8'),
          '거제도 숙소',
          '이순신 (거제도)',
          '010-9999-9999',
          '45454',
          '경상남도 거제시',
          '조선소 근처',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a699408e0711080d15a0125517b21'),
          hextoraw('019a698a43ea7c958b8ae2ed1625f1f8'),
          '서울 숙소',
          '이순신 (서울숙소)',
          '010-0000-0000',
          '56565',
          '서울시 동작구 노량진동',
          '오피스텔',
          current_timestamp
     FROM dual;

COMMIT;