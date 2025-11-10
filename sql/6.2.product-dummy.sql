INSERT INTO products (
   product_id,
   seller_id,
   category_id,
   product_price,
   product_stock,
   product_name,
   product_description,
   product_ai_summary,
   product_status,
   approved_date
)
-- Seller 1: 강철 상점 (노트북 7개)
   SELECT hextoraw('019a69f5c3c07412bc208e2345ea7203'),
          hextoraw('019a69f1f60c73ab8d829233d471597d'),
          hextoraw('019a69f3b7b479cda46161ee136c6801'),
          1490000,
          50,
          '강철 게이밍 노트북 15인치',
          '최신 9세대 CPU와 고성능 그래픽카드를 탑재한 게이밍 노트북입니다.',
          '15인치 화면의 고성능 게이밍 노트북. 최신 CPU 및 GPU 탑재.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c078feaff4e093db2e9ae1'),
          hextoraw('019a69f1f60c73ab8d829233d471597d'),
          hextoraw('019a69f3b7b479cda46161ee136c6801'),
          890000,
          100,
          '강철 사무용 노트북 13인치',
          '가벼운 무게와 오래 가는 배터리로 사무용, 학생용으로 최적화된 노트북입니다.',
          '13인치 경량 노트북. 긴 배터리 시간, 사무용 및 학생용 추천.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c072129588d633562e520c'),
          hextoraw('019a69f1f60c73ab8d829233d471597d'),
          hextoraw('019a69f3b7b479cda46161ee136c6801'),
          2100000,
          30,
          '강철 크리에이터 노트북 16인치',
          '4K OLED 디스플레이와 외장 GPU로 영상 편집 및 디자인 작업에 특화된 전문가용 노트북입니다.',
          '16인치 4K OLED 전문가용 노트북. 영상 편집 및 디자인 작업에 최적화.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07a7182aea2715abb802a'),
          hextoraw('019a69f1f60c73ab8d829233d471597d'),
          hextoraw('019a69f3b7b479cda46161ee136c6801'),
          750000,
          80,
          '강철 학생용 노트북 14인치',
          '인강 및 문서 작업에 충분한 성능을 갖춘 가성비 학생용 노트북입니다.',
          '14인치 가성비 노트북. 인강 시청 및 문서 작업에 적합.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c074a6adb5774f39fc7fc7'),
          hextoraw('019a69f1f60c73ab8d829233d471597d'),
          hextoraw('019a69f3b7b479cda46161ee136c6801'),
          1190000,
          40,
          '강철 울트라북 14인치',
          '1kg 미만의 초경량 무게와 세련된 디자인을 갖춘 프리미엄 울트라북입니다.',
          '1kg 미만 14인치 초경량 프리미엄 울트라북.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c077c2bbca24506b7a07c1'),
          hextoraw('019a69f1f60c73ab8d829233d471597d'),
          hextoraw('019a69f3b7b479cda46161ee136c6801'),
          1800000,
          20,
          '강철 2-in-1 노트북 13인치',
          '터치스크린과 360도 회전 힌지로 태블릿처럼 사용 가능한 노트북입니다.',
          '13인치 2-in-1 터치스크린 노트북. 360도 회전, 태블릿 겸용.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07d2698c2e7e60f725c16'),
          hextoraw('019a69f1f60c73ab8d829233d471597d'),
          hextoraw('019a69f3b7b479cda46161ee136c6801'),
          450000,
          150,
          '강철 크롬북 11인치',
          '웹 서핑과 클라우드 작업에 최적화된 저렴한 크롬북입니다.',
          '11인치 저가형 크롬북. 웹 서핑 및 클라우드 작업용.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
-- Seller 1: 강철 상점 (모바일 7개)
   SELECT hextoraw('019a69f5c3c07646b57ef56d0a09cd97'),
          hextoraw('019a69f1f60c73ab8d829233d471597d'),
          hextoraw('019a69f3b7b47a8fba32343d5068fae9'),
          1200000,
          200,
          '강철 스마트폰 V1',
          'AI 카메라와 초고속 프로세서를 탑재한 최신 플래그십 스마트폰.',
          '최신 플래그십 스마트폰. AI 카메라 및 고속 프로세서 탑재.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07bffbf72689179eaa8d9'),
          hextoraw('019a69f1f60c73ab8d829233d471597d'),
          hextoraw('019a69f3b7b47a8fba32343d5068fae9'),
          550000,
          300,
          '강철 스마트폰 A1 (보급형)',
          '대화면과 대용량 배터리를 탑재한 가성비 스마트폰.',
          '가성비 보급형 스마트폰. 대화면 및 대용량 배터리.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07565aa945c6e5d0b90f4'),
          hextoraw('019a69f1f60c73ab8d829233d471597d'),
          hextoraw('019a69f3b7b47a8fba32343d5068fae9'),
          880000,
          150,
          '강철 스마트폰 V1 (Mini)',
          'V1의 성능은 그대로, 한 손에 잡히는 컴팩트 사이즈 스마트폰.',
          '컴팩트 사이즈 플래그십 스마트폰. 고성능 유지.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07ac588b800c2d38b7dfe'),
          hextoraw('019a69f1f60c73ab8d829233d471597d'),
          hextoraw('019a69f3b7b47a8fba32343d5068fae9'),
          650000,
          100,
          '강철 태블릿 10인치',
          '영상 시청 및 필기에 최적화된 10인치 태블릿.',
          '10인치 태블릿. 영상 시청 및 필기용.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c071588ed724148e46011d'),
          hextoraw('019a69f1f60c73ab8d829233d471597d'),
          hextoraw('019a69f3b7b47a8fba32343d5068fae9'),
          990000,
          80,
          '강철 태블릿 12인치 (Pro)',
          '전문가용 드로잉 및 작업이 가능한 고성능 12인치 프로 태블릿.',
          '12인치 고성능 프로 태블릿. 전문가용 드로잉 및 작업용.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c076baa0fd3a121b5bae04'),
          hextoraw('019a69f1f60c73ab8d829233d471597d'),
          hextoraw('019a69f3b7b47a8fba32343d5068fae9'),
          199000,
          250,
          '강철 무선 이어폰',
          '노이즈 캔슬링을 지원하는 고음질 무선 이어폰.',
          '고음질 무선 이어폰. 노이즈 캔슬링 지원.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07f83bcef1a508d05263f'),
          hextoraw('019a69f1f60c73ab8d829233d471597d'),
          hextoraw('019a69f3b7b47a8fba32343d5068fae9'),
          100000,
          50,
          '스마트폰 보조배터리 20000mAh',
          '고속 충전을 지원하는 20000mAh 대용량 보조배터리.',
          '20000mAh 대용량 보조배터리. 고속 충전 지원.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
-- Seller 2: 친절한 영희네 (남성의류 7개)
   SELECT hextoraw('019a69f5c3c07d838b893fb3ed04228d'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b471ae9dffaa18f0005106'),
          59000,
          100,
          '영희네 남성 옥스포드 셔츠',
          '깔끔한 디자인의 화이트 옥스포드 셔츠입니다. (사이즈: L)',
          '남성용 화이트 옥스포드 셔츠 (L 사이즈)',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07d7aa40bc8bc7dd56c27'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b471ae9dffaa18f0005106'),
          89000,
          120,
          '영희네 남성 슬랙스 (블랙)',
          '신축성이 좋은 편안한 착용감의 블랙 슬랙스입니다. (사이즈: 32)',
          '남성용 블랙 슬랙스 (32 사이즈). 신축성 좋음.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c0725c85edf652b98f9cfb'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b471ae9dffaa18f0005106'),
          35000,
          200,
          '영희네 남성 반팔 티셔츠 (네이비)',
          '부드러운 면 소재의 기본 네이비 반팔 티셔츠입니다. (사이즈: XL)',
          '남성용 네이비 면 반팔 티셔츠 (XL 사이즈)',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c0710b8a316c1554761879'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b471ae9dffaa18f0005106'),
          120000,
          70,
          '영희네 남성 경량 패딩 조끼',
          '간절기에 입기 좋은 가벼운 남성용 패딩 조끼입니다. (색상: 그레이)',
          '남성용 경량 패딩 조끼 (그레이). 간절기용.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07668b27c62db89013277'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b471ae9dffaa18f0005106'),
          65000,
          100,
          '영희네 남성 트레이닝 팬츠',
          '활동성이 편한 조거 스타일의 트레이닝 팬츠입니다. (색상: 차콜)',
          '남성용 조거 트레이닝 팬츠 (차콜)',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c077a999e87c5c9545244a'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b471ae9dffaa18f0005106'),
          150000,
          50,
          '영희네 남성 울 코트 (싱글)',
          '겨울용 따뜻한 울 소재의 싱글 코트입니다. (색상: 블랙)',
          '남성용 블랙 울 싱글 코트. 겨울용.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07ad89e8e0c415c9f544c'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b471ae9dffaa18f0005106'),
          45000,
          130,
          '영희네 남성 후드 티셔츠 (그레이)',
          '기모 안감으로 따뜻한 베이직 후드 티셔츠입니다. (사이즈: L)',
          '남성용 기모 후드 티셔츠 (그레이, L 사이즈)',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
-- Seller 2: 친절한 영희네 (여성의류 7개)
   SELECT hextoraw('019a69f5c3c07caaa08e69d8ce1dd7cf'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b4743bb816e3a6973b47d1'),
          79000,
          150,
          '영희네 여성 쉬폰 원피스',
          '봄 신상 하늘하늘한 쉬폰 롱 원피스입니다. (색상: 핑크)',
          '여성용 핑크 쉬폰 롱 원피스. 봄 신상.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07a938327609bd7a9752b'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b4743bb816e3a6973b47d1'),
          99000,
          100,
          '영희네 여성 트위드 자켓',
          '격식 있는 자리에 어울리는 클래식 트위드 자켓입니다. (색상: 아이보리)',
          '여성용 아이보리 트위드 자켓. 클래식 스타일.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07a5188344e08a111fdb1'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b4743bb816e3a6973b47d1'),
          55000,
          130,
          '영희네 여성 와이드 슬랙스 (베이지)',
          '편안하고 스타일리시한 와이드 핏 여성 슬랙스입니다. (사이즈: M)',
          '여성용 베이지 와이드 슬랙스 (M 사이즈)',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c0710a9700a3e8d43cf1ee'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b4743bb816e3a6973b47d1'),
          42000,
          180,
          '영희네 여성 블라우스 (스카이블루)',
          '오피스룩으로 입기 좋은 부드러운 소재의 블라우스입니다. (사이즈: 55)',
          '여성용 스카이블루 블라우스 (55 사이즈). 오피스룩 추천.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c0764c87e0c37dff3f8891'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b4743bb816e3a6973b47d1'),
          88000,
          90,
          '영희네 여성 데님 스커트 (롱)',
          'A라인으로 퍼지는 청순한 스타일의 롱 데님 스커트입니다.',
          '여성용 A라인 롱 데님 스커트',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c076ca8a972fe1fc785328'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b4743bb816e3a6973b47d1'),
          130000,
          60,
          '영희네 여성 트렌치 코트 (베이지)',
          '가을 필수 아이템, 클래식 디자인의 여성 트렌치 코트입니다.',
          '여성용 베이지 클래식 트렌치 코트',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c0732fb9bd2a7ab2f35ba3'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b4743bb816e3a6973b47d1'),
          29000,
          200,
          '영희네 여성 기본 가디건 (블랙)',
          '어디에나 걸치기 좋은 기본 블랙 가디건입니다. (사이즈: Free)',
          '여성용 기본 블랙 가디건 (Free 사이즈)',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
-- Seller 2: 친절한 영희네 (패션잡화 7개)
   SELECT hextoraw('019a69f5c3c0743ab124096280c2cfa6'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b4711e8cc7eba3d7f29fdf'),
          120000,
          80,
          '영희네 천연 소가죽 벨트 (브라운)',
          '클래식한 디자인의 천연 소가죽 남성 벨트입니다.',
          '남성용 브라운 천연 소가죽 벨트',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c073f8a8489378f3c1af02'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b4711e8cc7eba3d7f29fdf'),
          180000,
          70,
          '영희네 캔버스 크로스백 (아이보리)',
          '가볍고 수납이 용이한 아이보리 캔버스 크로스백입니다.',
          '아이보리 캔버스 크로스백. 가볍고 수납 용이.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07673a6d534699d4b2d60'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b4711e8cc7eba3d7f29fdf'),
          45000,
          150,
          '영희네 볼캡 (베이지)',
          '심플한 로고 디자인의 베이직 베이지 볼캡입니다.',
          '베이직 베이지 볼캡. 심플 로고.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c077c39c026eb5d8e34797'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b4711e8cc7eba3d7f29fdf'),
          210000,
          60,
          '영희네 여성 장지갑 (레드)',
          '고급스러운 가죽 소재의 여성용 레드 장지갑입니다.',
          '여성용 레드 가죽 장지갑',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07c23a7dd0c541cb65a52'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b4711e8cc7eba3d7f29fdf'),
          78000,
          100,
          '영희네 실크 스카프 (블루)',
          '부드러운 100% 실크 소재의 패턴 스카프입니다.',
          '100% 실크 패턴 스카프 (블루)',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07d7083ff88279e50ec05'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b4711e8cc7eba3d7f29fdf'),
          15000,
          300,
          '영희네 패션 양말 5종 세트',
          '다양한 디자인의 패션 양말 5종 세트입니다.',
          '패션 양말 5종 세트',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07a93b5add7ae6588c162'),
          hextoraw('019a69f1f60c7e359f910f41aef5f35f'),
          hextoraw('019a69f3b7b4711e8cc7eba3d7f29fdf'),
          99000,
          80,
          '영희네 선글라스 (블랙)',
          'UV 차단 기능이 있는 베이직 블랙 선글라스입니다.',
          '베이직 블랙 선글라스 (UV 차단)',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
-- Seller 3: 산소탱크 스포츠 (캠핑용품 8개)
   SELECT hextoraw('019a69f5c3c07cf29f8b69058c4e4b1c'),
          hextoraw('019a69f1f60c76ea9cbdafb048644daf'),
          hextoraw('019a69f3b7b476b3b1de1994732363db'),
          250000,
          30,
          '산소탱크 원터치 텐트 (4인용)',
          '설치가 간편한 4인용 방수 원터치 텐트입니다.',
          '4인용 방수 원터치 텐트. 간편 설치.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c070a08693306c227d5cbe'),
          hextoraw('019a69f1f60c76ea9cbdafb048644daf'),
          hextoraw('019a69f3b7b476b3b1de1994732363db'),
          80000,
          50,
          '산소탱크 캠핑 의자 (릴렉스 체어)',
          '편안하게 기댈 수 있는 릴렉스 캠핑 의자입니다. (색상: 네이비)',
          '네이비 색상 릴렉스 캠핑 의자. 편안한 휴식 가능.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c071a9871fcacd8633c8bb'),
          hextoraw('019a69f1f60c76ea9cbdafb048644daf'),
          hextoraw('019a69f3b7b476b3b1de1994732363db'),
          120000,
          40,
          '산소탱크 캠핑 테이블 (접이식)',
          '알루미늄 소재의 가벼운 접이식 캠핑 테이블입니다. (사이즈: L)',
          'L사이즈 경량 알루미늄 접이식 캠핑 테이블.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07bf89bd30b7eda79b84b'),
          hextoraw('019a69f1f60c76ea9cbdafb048644daf'),
          hextoraw('019a69f3b7b476b3b1de1994732363db'),
          95000,
          60,
          '산소탱크 캠핑용 침낭 (사계절)',
          '사계절 사용 가능한 고성능 캠핑용 침낭입니다.',
          '사계절용 고성능 캠핑 침낭.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07959a603f710fd030402'),
          hextoraw('019a69f1f60c76ea9cbdafb048644daf'),
          hextoraw('019a69f3b7b476b3b1de1994732363db'),
          65000,
          70,
          '산소탱크 캠핑용 랜턴 (LED)',
          '밝기 조절이 가능한 충전식 LED 캠핑 랜턴입니다.',
          '충전식 LED 캠핑 랜턴. 밝기 조절 가능.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c0782c9ca55fbf8fc7e750'),
          hextoraw('019a69f1f60c76ea9cbdafb048644daf'),
          hextoraw('019a69f3b7b476b3b1de1994732363db'),
          150000,
          30,
          '산소탱크 캠핑용 화로대',
          '불멍과 바베큐가 가능한 스테인레스 화로대입니다.',
          '스테인레스 캠핑 화로대. 불멍 및 바베큐 가능.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c076c080120ccafdbcd586'),
          hextoraw('019a69f1f60c76ea9cbdafb048644daf'),
          hextoraw('019a69f3b7b476b3b1de1994732363db'),
          180000,
          25,
          '산소탱크 아이스박스 50L',
          '보냉력이 뛰어난 50L 대용량 아이스박스입니다.',
          '50L 대용량 아이스박스. 뛰어난 보냉력.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c071ab8ec90ab9db76a800'),
          hextoraw('019a69f1f60c76ea9cbdafb048644daf'),
          hextoraw('019a69f3b7b476b3b1de1994732363db'),
          45000,
          100,
          '산소탱크 캠핑용 코펠 (4인용)',
          '경질 알루미늄 소재의 4인용 코펠 세트입니다.',
          '4인용 경질 알루미늄 코펠 세트.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
-- Seller 3: 산소탱크 스포츠 (축구용품 7개)
   SELECT hextoraw('019a69f5c3c0712994ed97c51816d0a2'),
          hextoraw('019a69f1f60c76ea9cbdafb048644daf'),
          hextoraw('019a69f3b7b47e4e83b7394018b33917'),
          150000,
          50,
          '산소탱크 축구공 (K-리그 공인구)',
          '프로 경기용 K-리그 공인구입니다. (5호)',
          'K-리그 공인구 5호. 프로 경기용.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07fa5932ab1b9515efa64'),
          hextoraw('019a69f1f60c76ea9cbdafb048644daf'),
          hextoraw('019a69f3b7b47e4e83b7394018b33917'),
          180000,
          40,
          '산소탱크 축구화 (FG 스터드)',
          '천연 잔디용 FG 스터드 축구화입니다. (사이즈: 270mm)',
          '천연 잔디용 FG 스터드 축구화 (270mm)',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c0769fb19f89840437d235'),
          hextoraw('019a69f1f60c76ea9cbdafb048644daf'),
          hextoraw('019a69f3b7b47e4e83b7394018b33917'),
          160000,
          45,
          '산소탱크 축구화 (TF 스터드)',
          '인조 잔디용 TF 스터드 풋살화입니다. (사이즈: 270mm)',
          '인조 잔디용 TF 스터드 풋살화 (270mm)',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c076d29b87cd61fd7c9189'),
          hextoraw('019a69f1f60c76ea9cbdafb048644daf'),
          hextoraw('019a69f3b7b47e4e83b7394018b33917'),
          75000,
          80,
          '산소탱크 골키퍼 장갑 (프로용)',
          '그립감이 뛰어난 프로페셔널 등급의 골키퍼 장갑입니다. (사이즈: 9호)',
          '프로 등급 골키퍼 장갑 (9호). 뛰어난 그립감.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07e5f98d88b4833f6c54a'),
          hextoraw('019a69f1f60c76ea9cbdafb048644daf'),
          hextoraw('019a69f3b7b47e4e83b7394018b33917'),
          45000,
          100,
          '산소탱크 축구 유니폼 (레드)',
          '땀 배출이 용이한 기능성 축구 유니폼 상하의 세트입니다. (사이즈: L)',
          '기능성 축구 유니폼 상하의 세트 (레드, L)',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07e18bea1e5194a9ab10e'),
          hextoraw('019a69f1f60c76ea9cbdafb048644daf'),
          hextoraw('019a69f3b7b47e4e83b7394018b33917'),
          35000,
          120,
          '산소탱크 스타킹 (블랙)',
          '미끄럼 방지 패드가 부착된 논슬립 축구 스타킹입니다.',
          '논슬립 축구 스타킹 (블랙). 미끄럼 방지 패드 부착.',
          'APPROVED',
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f5c3c07cdb98e60b09a59e0f72'),
          hextoraw('019a69f1f60c76ea9cbdafb048644daf'),
          hextoraw('019a69f3b7b47e4e83b7394018b33917'),
          25000,
          150,
          '산소탱크 정강이 보호대',
          '충격 흡수가 뛰어난 경량 정강이 보호대입니다. (사이즈: M)',
          '경량 정강이 보호대 (M 사이즈). 충격 흡수.',
          'APPROVED',
          current_timestamp
     FROM dual;

COMMIT;