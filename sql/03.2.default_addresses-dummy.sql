INSERT INTO default_addresses (
   user_id,
   address_id
)
   SELECT hextoraw('019a698a43ea778587a64ba7e9e58784'),
          hextoraw('019a698d82c07c66ac4a293c84acfa52')
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698a43ea7dca8b34cde9a3850adb'),
          hextoraw('019a698d82c07499b33d51a99e798e68')
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698a43ea7ba38634d17b296bd88c'),
          hextoraw('019a698d82c079b1907acd71118370aa')
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698a43ea784389bd8d1b6e9a5cfe'),
          hextoraw('019a698d82c070e99f2a30bbf500e1ab')
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698a43ea7f6db7d6b0682abbd378'),
          hextoraw('019a698d82c077d5aa4f70751fece6d7')
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698a43ea75a7ae59bcec4f0361ac'),
          hextoraw('019a698d82c07d549e272b0e0ae113ab')
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698a43ea7e4aa01065cd58d2ccd8'),
          hextoraw('019a698d82c07ab0b452942f32cfa5dc')
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698a43ea7b279103a70f0065184a'),
          hextoraw('019a698d82c07e6bb1a316575c02472a')
     FROM dual
   UNION ALL
   SELECT hextoraw('019a698a43ea7c958b8ae2ed1625f1f8'),
          hextoraw('019a698d82c07ebc8d299ee1144b2660')
     FROM dual;

COMMIT;