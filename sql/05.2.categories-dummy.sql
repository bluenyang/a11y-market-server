INSERT INTO categories (
   category_id,
   parent_cat_id,
   category_name
)
   SELECT hextoraw('019a69f3b7b474b4902d651d1f11d323'),
          NULL,
          '디지털/가전'
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f3b7b47c2dafb22fcb168bfe12'),
          NULL,
          '패션/의류'
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f3b7b4725384472516faee63f4'),
          NULL,
          '스포츠/레저'
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f3b7b479cda46161ee136c6801'),
          hextoraw('019a69f3b7b474b4902d651d1f11d323'),
          '노트북'
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f3b7b47a8fba32343d5068fae9'),
          hextoraw('019a69f3b7b474b4902d651d1f11d323'),
          '모바일'
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f3b7b471ae9dffaa18f0005106'),
          hextoraw('019a69f3b7b47c2dafb22fcb168bfe12'),
          '남성의류'
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f3b7b4743bb816e3a6973b47d1'),
          hextoraw('019a69f3b7b47c2dafb22fcb168bfe12'),
          '여성의류'
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f3b7b4711e8cc7eba3d7f29fdf'),
          hextoraw('019a69f3b7b47c2dafb22fcb168bfe12'),
          '패션잡화'
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f3b7b476b3b1de1994732363db'),
          hextoraw('019a69f3b7b4725384472516faee63f4'),
          '캠핑용품'
     FROM dual
   UNION ALL
   SELECT hextoraw('019a69f3b7b47e4e83b7394018b33917'),
          hextoraw('019a69f3b7b4725384472516faee63f4'),
          '축구용품'
     FROM dual;

COMMIT;