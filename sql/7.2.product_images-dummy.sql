INSERT INTO product_images (
   image_id,
   product_id,
   image_url,
   alt_text,
   created_at
)
   SELECT hextoraw('019a69fb7a5a76b6af6f1b824ea5b9f0'),
          hextoraw('019a69f5c3c07412bc208e2345ea7203'),
          'https://minio.bluenyang.kr/test-images/img%2F1001.jpg',
          '강철 게이밍 노트북 15인치 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5a7dcfb09730d3aa8f2f02'),
          hextoraw('019a69f5c3c078feaff4e093db2e9ae1'),
          'https://minio.bluenyang.kr/test-images/img%2F1002.jpg',
          '강철 사무용 노트북 13인치 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5a727c80ccfb317f5a0dab'),
          hextoraw('019a69f5c3c072129588d633562e520c'),
          'https://minio.bluenyang.kr/test-images/img%2F1003.jpg',
          '강철 크리에이터 노트북 16인치 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5a7d19966f4e2a7d6552c2'),
          hextoraw('019a69f5c3c07a7182aea2715abb802a'),
          'https://minio.bluenyang.kr/test-images/img%2F1004.jpg',
          '강철 학생용 노트북 14인치 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5a7b9dbfadfc2c1b6d2d8b'),
          hextoraw('019a69f5c3c074a6adb5774f39fc7fc7'),
          'https://minio.bluenyang.kr/test-images/img%2F1005.jpg',
          '강철 울트라북 14인치 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5a7a70962c9e6b7c4c0b35'),
          hextoraw('019a69f5c3c077c2bbca24506b7a07c1'),
          'https://minio.bluenyang.kr/test-images/img%2F1006.jpg',
          '강철 2-in-1 노트북 13인치 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5a75b08b41801e2cef4f3c'),
          hextoraw('019a69f5c3c07d2698c2e7e60f725c16'),
          'https://minio.bluenyang.kr/test-images/img%2F1007.jpg',
          '강철 크롬북 11인치 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5a72a4b4d35fc2fc6b01ae'),
          hextoraw('019a69f5c3c07646b57ef56d0a09cd97'),
          'https://minio.bluenyang.kr/test-images/img%2F1008.jpg',
          '강철 스마트폰 V1 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5a7554b9f8bff75879fe9e'),
          hextoraw('019a69f5c3c07bffbf72689179eaa8d9'),
          'https://minio.bluenyang.kr/test-images/img%2F1009.jpg',
          '강철 스마트폰 A1 (보급형) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5a796d867c081840793315'),
          hextoraw('019a69f5c3c07565aa945c6e5d0b90f4'),
          'https://minio.bluenyang.kr/test-images/img%2F1010.jpg',
          '강철 스마트폰 V1 (Mini) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b748e97d58f84c2657432'),
          hextoraw('019a69f5c3c07ac588b800c2d38b7dfe'),
          'https://minio.bluenyang.kr/test-images/img%2F1011.jpg',
          '강철 태블릿 10인치 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b718ebe7917b16430156f'),
          hextoraw('019a69f5c3c071588ed724148e46011d'),
          'https://minio.bluenyang.kr/test-images/img%2F1012.jpg',
          '강철 태블릿 12인치 (Pro) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7f48bcde4a2de95cd414'),
          hextoraw('019a69f5c3c076baa0fd3a121b5bae04'),
          'https://minio.bluenyang.kr/test-images/img%2F1013.jpg',
          '강철 무선 이어폰 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7955a7a19585ff96d1e2'),
          hextoraw('019a69f5c3c07f83bcef1a508d05263f'),
          'https://minio.bluenyang.kr/test-images/img%2F1014.jpg',
          NULL,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b765e9b6cfd521018db29'),
          hextoraw('019a69f5c3c07d838b893fb3ed04228d'),
          'https://minio.bluenyang.kr/test-images/img%2F1015.jpg',
          NULL,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7c02b064fe849dfa57a7'),
          hextoraw('019a69f5c3c07d7aa40bc8bc7dd56c27'),
          'https://minio.bluenyang.kr/test-images/img%2F1001.jpg',
          NULL,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b751bb03fb390d7a71add'),
          hextoraw('019a69f5c3c0725c85edf652b98f9cfb'),
          'https://minio.bluenyang.kr/test-images/img%2F1002.jpg',
          NULL,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b76728c9d617dd9dc14ba'),
          hextoraw('019a69f5c3c0710b8a316c1554761879'),
          'https://minio.bluenyang.kr/test-images/img%2F1003.jpg',
          '영희네 남성 경량 패딩 조끼 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b72bb874b1205e1c335fd'),
          hextoraw('019a69f5c3c07668b27c62db89013277'),
          'https://minio.bluenyang.kr/test-images/img%2F1004.jpg',
          '영희네 남성 트레이닝 팬츠 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b795099d38bc1574bc755'),
          hextoraw('019a69f5c3c077a999e87c5c9545244a'),
          'https://minio.bluenyang.kr/test-images/img%2F1005.jpg',
          '영희네 남성 울 코트 (싱글) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b734288552d29dad0c0ad'),
          hextoraw('019a69f5c3c07ad89e8e0c415c9f544c'),
          'https://minio.bluenyang.kr/test-images/img%2F1006.jpg',
          '영희네 남성 후드 티셔츠 (그레이) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7c9bbd03fceea0a086b2'),
          hextoraw('019a69f5c3c07caaa08e69d8ce1dd7cf'),
          'https://minio.bluenyang.kr/test-images/img%2F1007.jpg',
          '영희네 여성 쉬폰 원피스 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b78729af18974faa7fd48'),
          hextoraw('019a69f5c3c07a938327609bd7a9752b'),
          'https://minio.bluenyang.kr/test-images/img%2F1008.jpg',
          '영희네 여성 트위드 자켓 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7f079d428ddac6c8bd82'),
          hextoraw('019a69f5c3c07a5188344e08a111fdb1'),
          'https://minio.bluenyang.kr/test-images/img%2F1009.jpg',
          '영희네 여성 와이드 슬랙스 (베이지) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b742d97883ca2efe21b86'),
          hextoraw('019a69f5c3c0710a9700a3e8d43cf1ee'),
          'https://minio.bluenyang.kr/test-images/img%2F1010.jpg',
          '영희네 여성 블라우스 (스카이블루) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7b95b048c5c0cea7d170'),
          hextoraw('019a69f5c3c0764c87e0c37dff3f8891'),
          'https://minio.bluenyang.kr/test-images/img%2F1011.jpg',
          '영희네 여성 데님 스커트 (롱) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7205bce022f5a1b31bf2'),
          hextoraw('019a69f5c3c076ca8a972fe1fc785328'),
          'https://minio.bluenyang.kr/test-images/img%2F1012.jpg',
          '영희네 여성 트렌치 코트 (베이지) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7ba594797c17b024cba0'),
          hextoraw('019a69f5c3c0732fb9bd2a7ab2f35ba3'),
          'https://minio.bluenyang.kr/test-images/img%2F1013.jpg',
          '영희네 여성 기본 가디건 (블랙) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7753a4e8e9bfd98ff1af'),
          hextoraw('019a69f5c3c0743ab124096280c2cfa6'),
          'https://minio.bluenyang.kr/test-images/img%2F1014.jpg',
          '영희네 천연 소가죽 벨트 (브라운) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7f9d8c4a142b3a05925d'),
          hextoraw('019a69f5c3c073f8a8489378f3c1af02'),
          'https://minio.bluenyang.kr/test-images/img%2F1015.jpg',
          '영희네 캔버스 크로스백 (아이보리) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7413b082ded3a69d688b'),
          hextoraw('019a69f5c3c07673a6d534699d4b2d60'),
          'https://minio.bluenyang.kr/test-images/img%2F1001.jpg',
          '영희네 볼캡 (베이지) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b750285ab1023a36679c6'),
          hextoraw('019a69f5c3c077c39c026eb5d8e34797'),
          'https://minio.bluenyang.kr/test-images/img%2F1002.jpg',
          '영희네 여성 장지갑 (레드) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7d16940a54ad9f8251ef'),
          hextoraw('019a69f5c3c07c23a7dd0c541cb65a52'),
          'https://minio.bluenyang.kr/test-images/img%2F1003.jpg',
          '영희네 실크 스카프 (블루) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b70d9b82cbda82656c2fc'),
          hextoraw('019a69f5c3c07d7083ff88279e50ec05'),
          'https://minio.bluenyang.kr/test-images/img%2F1004.jpg',
          '영희네 패션 양말 5종 세트 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7d06a4d2665904bb00b7'),
          hextoraw('019a69f5c3c07a93b5add7ae6588c162'),
          'https://minio.bluenyang.kr/test-images/img%2F1005.jpg',
          '영희네 선글라스 (블랙) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7050b59ae7b3e4920ac7'),
          hextoraw('019a69f5c3c07cf29f8b69058c4e4b1c'),
          'https://minio.bluenyang.kr/test-images/img%2F1006.jpg',
          '산소탱크 원터치 텐트 (4인용) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7f27bf614eaf05b31f55'),
          hextoraw('019a69f5c3c070a08693306c227d5cbe'),
          'https://minio.bluenyang.kr/test-images/img%2F1007.jpg',
          '산소탱크 캠핑 의자 (릴렉스 체어) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b77da90e8da59b0ba033e'),
          hextoraw('019a69f5c3c071a9871fcacd8633c8bb'),
          'https://minio.bluenyang.kr/test-images/img%2F1008.jpg',
          '산소탱크 캠핑 테이블 (접이식) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b769f933e1169063086e8'),
          hextoraw('019a69f5c3c07bf89bd30b7eda79b84b'),
          'https://minio.bluenyang.kr/test-images/img%2F1009.jpg',
          '산소탱크 캠핑용 침낭 (사계절) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7d3b909d3aef1f818ae9'),
          hextoraw('019a69f5c3c07959a603f710fd030402'),
          'https://minio.bluenyang.kr/test-images/img%2F1010.jpg',
          '산소탱크 캠핑용 랜턴 (LED) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7e66b96bdaf30f609278'),
          hextoraw('019a69f5c3c0782c9ca55fbf8fc7e750'),
          'https://minio.bluenyang.kr/test-images/img%2F1011.jpg',
          '산소탱크 캠핑용 화로대 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b74248be8f6a9a0a101e2'),
          hextoraw('019a69f5c3c076c080120ccafdbcd586'),
          'https://minio.bluenyang.kr/test-images/img%2F1012.jpg',
          '산소탱크 아이스박스 50L 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b78779c6932d9c80c06f4'),
          hextoraw('019a69f5c3c071ab8ec90ab9db76a800'),
          'https://minio.bluenyang.kr/test-images/img%2F1013.jpg',
          NULL,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7fcd9442f6cd3b79c1de'),
          hextoraw('019a69f5c3c0712994ed97c51816d0a2'),
          'https://minio.bluenyang.kr/test-images/img%2F1014.jpg',
          '산소탱크 축구공 (K-리그 공인구) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b71c88e992b3cfbc3bfad'),
          hextoraw('019a69f5c3c07fa5932ab1b9515efa64'),
          'https://minio.bluenyang.kr/test-images/img%2F1015.jpg',
          NULL,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7f0ca094ad1c9b9c227b'),
          hextoraw('019a69f5c3c0769fb19f89840437d235'),
          'https://minio.bluenyang.kr/test-images/img%2F1001.jpg',
          NULL,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b79d9a491dae792dc06e2'),
          hextoraw('019a69f5c3c076d29b87cd61fd7c9189'),
          'https://minio.bluenyang.kr/test-images/img%2F1002.jpg',
          NULL,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b71618f858cc502d5c8f6'),
          hextoraw('019a69f5c3c07e5f98d88b4833f6c54a'),
          'https://minio.bluenyang.kr/test-images/img%2F1003.jpg',
          NULL,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7df6b34a95b1281a7b8'),
          hextoraw('019a69f5c3c07e18bea1e5194a9ab10e'),
          'https://minio.bluenyang.kr/test-images/img%2F1004.jpg',
          '산소탱크 스타킹 (블랙) 대표 이미지',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69fb7a5b7b778ce106e150985ad7'),
          hextoraw('019a69f5c3c07cdb98e60b09a59e0f72'),
          'https://minio.bluenyang.kr/test-images/img%2F1005.jpg',
          '산소탱크 정강이 보호대 대표 이미지',
          current_timestamp
     FROM dual;

COMMIT;