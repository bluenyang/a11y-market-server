INSERT INTO order_items (
   order_item_id,
   order_id,
   product_id,
   product_name,
   product_price,
   product_quantity,
   order_item_status,
   cancel_reason
)
   SELECT hextoraw('019a6a127f227fec9029bdbb9f4aa720'),
          hextoraw('019a69ef120970ef8f929678c20cf1f0'),
          hextoraw('019a69f5c3c07412bc208e2345ea7203'),
          '강철 게이밍 노트북 15인치',
          1490000,
          1,
          'PAID',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f227b6e9e952708025a9b56'),
          hextoraw('019a69ef120970ef8f929678c20cf1f0'),
          hextoraw('019a69f5c3c076baa0fd3a121b5bae04'),
          '강철 무선 이어폰',
          199000,
          1,
          'PAID',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f22718282278c54803a52d1'),
          hextoraw('019a69ef120970ef8f929678c20cf1f0'),
          hextoraw('019a69f5c3c07f83bcef1a508d05263f'),
          '스마트폰 보조배터리 20000mAh',
          100000,
          2,
          'PAID',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f2271eeabb7bc7e1011db4f'),
          hextoraw('019a69ef120972d2a5eb3782f9d74394'),
          hextoraw('019a69f5c3c07caaa08e69d8ce1dd7cf'),
          '영희네 여성 쉬폰 원피스',
          79000,
          1,
          'SHIPPED',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f227d84967c108cbd6c36df'),
          hextoraw('019a69ef120972d2a5eb3782f9d74394'),
          hextoraw('019a69f5c3c07a938327609bd7a9752b'),
          '영희네 여성 트위드 자켓',
          99000,
          1,
          'SHIPPED',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f227d0d8c8b663cc2fe982a'),
          hextoraw('019a69ef120972d2a5eb3782f9d74394'),
          hextoraw('019a69f5c3c07c23a7dd0c541cb65a52'),
          '영희네 실크 스카프 (블루)',
          78000,
          1,
          'SHIPPED',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f22729bbd244989c2e9fe78'),
          hextoraw('019a69ef120970008ca16118c0da4ef1'),
          hextoraw('019a69f5c3c0712994ed97c51816d0a2'),
          '산소탱크 축구공 (K-리그 공인구)',
          150000,
          1,
          'PAID',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f227abfb7ffc47643bc1a08'),
          hextoraw('019a69ef120970008ca16118c0da4ef1'),
          hextoraw('019a69f5c3c07cdb98e60b09a59e0f72'),
          '산소탱크 정강이 보호대',
          25000,
          2,
          'PAID',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f227211b3a9d9e7a592184f'),
          hextoraw('019a69ef12097ef5ab7423c9c6db1b3a'),
          hextoraw('019a69f5c3c07d838b893fb3ed04228d'),
          '영희네 남성 옥스포드 셔츠',
          59000,
          1,
          'CONFIRMED',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f227d4598e33e9a45ce1eb2'),
          hextoraw('019a69ef12097ef5ab7423c9c6db1b3a'),
          hextoraw('019a69f5c3c07d7aa40bc8bc7dd56c27'),
          '영희네 남성 슬랙스 (블랙)',
          89000,
          1,
          'CONFIRMED',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f2270158679332fee8d479e'),
          hextoraw('019a69ef12097c25bf970cd1a4297387'),
          hextoraw('019a69f5c3c07a5188344e08a111fdb1'),
          '영희네 여성 와이드 슬랙스 (베이지)',
          55000,
          1,
          'CANCEL_PENDING',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f22709f83a8f3a3420c8dfe'),
          hextoraw('019a69ef12097c25bf970cd1a4297387'),
          hextoraw('019a69f5c3c0710a9700a3e8d43cf1ee'),
          '영희네 여성 블라우스 (스카이블루)',
          42000,
          1,
          'PAID',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f227fb696911e9b8e8dbbf2'),
          hextoraw('019a69ef12097c25bf970cd1a4297387'),
          hextoraw('019a69f5c3c0732fb9bd2a7ab2f35ba3'),
          '영희네 여성 기본 가디건 (블랙)',
          29000,
          1,
          'PAID',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f2277baa260f4c02a72893c'),
          hextoraw('019a69ef12097a44a10534f077af76b8'),
          hextoraw('019a69f5c3c07fa5932ab1b9515efa64'),
          '산소탱크 축구화 (FG 스터드)',
          180000,
          1,
          'PAID',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f227d189bca97a9e01e4a3c'),
          hextoraw('019a69ef120976b4be402c2049a835f3'),
          hextoraw('019a69f5c3c07d2698c2e7e60f725c16'),
          '강철 크롬북 11인치',
          450000,
          1,
          'CONFIRMED',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f2274c9a2b3db3ba861623b'),
          hextoraw('019a69ef120976b4be402c2049a835f3'),
          hextoraw('019a69f5c3c07646b57ef56d0a09cd97'),
          '강철 스마트폰 V1',
          1200000,
          1,
          'CONFIRMED',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f227af69a8abe7fb034298f'),
          hextoraw('019a69ef120978f6bd6bc652586fb59d'),
          hextoraw('019a69f5c3c073f8a8489378f3c1af02'),
          '영희네 캔버스 크로스백 (아이보리)',
          180000,
          1,
          'SHIPPED',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f227b2a8198f87199b064ae'),
          hextoraw('019a69ef120978f6bd6bc652586fb59d'),
          hextoraw('019a69f5c3c07673a6d534699d4b2d60'),
          '영희네 볼캡 (베이지)',
          45000,
          2,
          'SHIPPED',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f227eaf8624b6bf17b0de49'),
          hextoraw('019a69ef120978f6bd6bc652586fb59d'),
          hextoraw('019a69f5c3c07d7083ff88279e50ec05'),
          '영희네 패션 양말 5종 세트',
          15000,
          3,
          'SHIPPED',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f227a52bc8feb8313e5a3e9'),
          hextoraw('019a69ef1209708a824e0e46e9608bfa'),
          hextoraw('019a69f5c3c070a08693306c227d5cbe'),
          '산소탱크 캠핑 의자 (릴렉스 체어)',
          80000,
          2,
          'PAID',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f2275e291a10d11b7104e40'),
          hextoraw('019a69ef1209708a824e0e46e9608bfa'),
          hextoraw('019a69f5c3c07959a603f710fd030402'),
          '산소탱크 캠핑용 랜턴 (LED)',
          65000,
          1,
          'PAID',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f22739db63dfac84e84226d'),
          hextoraw('019a69ef120973cd855c4ea2bc0bb5f6'),
          hextoraw('019a69f5c3c07cf29f8b69058c4e4b1c'),
          '산소탱크 원터치 텐트 (4인용)',
          250000,
          1,
          'CONFIRMED',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f2272e68b12a0c1e274218b'),
          hextoraw('019a69ef120973cd855c4ea2bc0bb5f6'),
          hextoraw('019a69f5c3c07bf89bd30b7eda79b84b'),
          '산소탱크 캠핑용 침낭 (사계절)',
          95000,
          2,
          'CONFIRMED',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f22737c9dd1426cdadf802f'),
          hextoraw('019a69ef120973cd855c4ea2bc0bb5f6'),
          hextoraw('019a69f5c3c071ab8ec90ab9db76a800'),
          '산소탱크 캠핑용 코펠 (4인용)',
          45000,
          1,
          'CONFIRMED',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f227345a76a42f3588e6520'),
          hextoraw('019a69ef12097c13808bd2aeb55c0007'),
          hextoraw('019a69f5c3c078feaff4e093db2e9ae1'),
          '강철 사무용 노트북 13인치',
          890000,
          1,
          'PAID',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f22789d87dc199b85569b50'),
          hextoraw('019a69ef12097c13808bd2aeb55c0007'),
          hextoraw('019a69f5c3c076baa0fd3a121b5bae04'),
          '강철 무선 이어폰',
          199000,
          1,
          'PAID',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f2278f7889396a715ebc4a2'),
          hextoraw('019a69ef12097b18be006047fdb2b94c'),
          hextoraw('019a69f5c3c07ad89e8e0c415c9f544c'),
          '영희네 남성 후드 티셔츠 (그레이)',
          45000,
          2,
          'CANCELED',
          '단순 변심으로 인한 취소'
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f2273f9aaee504131d5e567'),
          hextoraw('019a69ef1209739481ee148dc3539d56'),
          hextoraw('019a69f5c3c0769fb19f89840437d235'),
          '산소탱크 축구화 (TF 스터드)',
          160000,
          1,
          'PAID',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f227a43b1c85df68544b99d'),
          hextoraw('019a69ef1209739481ee148dc3539d56'),
          hextoraw('019a69f5c3c076d29b87cd61fd7c9189'),
          '산소탱크 골키퍼 장갑 (프로용)',
          75000,
          1,
          'PAID',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f22773f8d4017aaf426e744'),
          hextoraw('019a69ef1209739481ee148dc3539d56'),
          hextoraw('019a69f5c3c07e18bea1e5194a9ab10e'),
          '산소탱크 스타킹 (블랙)',
          35000,
          2,
          'PAID',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f2277029863506be8a40bb0'),
          hextoraw('019a69ef12097d3a8bbdfa45b0c9f568'),
          hextoraw('019a69f5c3c07668b27c62db89013277'),
          '영희네 남성 트레이닝 팬츠',
          65000,
          1,
          'SHIPPED',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f227d99b95142efb711cefa'),
          hextoraw('019a69ef12097d3a8bbdfa45b0c9f568'),
          hextoraw('019a69f5c3c07a93b5add7ae6588c162'),
          '영희네 선글라스 (블랙)',
          99000,
          1,
          'SHIPPED',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f227cd597098f09a25644a5'),
          hextoraw('019a69ef12097b71bbcd641190f9341e'),
          hextoraw('019a69f5c3c07ac588b800c2d38b7dfe'),
          '강철 태블릿 10인치',
          650000,
          1,
          'PAID',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f227680843c094b5612fa00'),
          hextoraw('019a69ef12097b71bbcd641190f9341e'),
          hextoraw('019a69f5c3c076baa0fd3a121b5bae04'),
          '강철 무선 이어폰',
          199000,
          1,
          'PAID',
          NULL
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a127f227005821a08a78361813b'),
          hextoraw('019a69ef12097b71bbcd641190f9341e'),
          hextoraw('019a69f5c3c07f83bcef1a508d05263f'),
          '스마트폰 보조배터리 20000mAh',
          100000,
          1,
          'PAID',
          NULL
     FROM dual;

COMMIT;