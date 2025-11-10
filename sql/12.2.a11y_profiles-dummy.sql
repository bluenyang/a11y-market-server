INSERT INTO a11y_profiles (
   profile_id,
   user_id,
   profile_name,
   font_size,
   contrast,
   text_to_speech,
   updated_at
)
   SELECT hextoraw('019a6a1dbb5376ffbe614320b8d6b31f'),
          hextoraw('019a698a43ea7dca8b34cde9a3850adb'),
          '기본 설정',
          'default',
          'default',
          0,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a1dbb5374c58002df267ebbfae4'),
          hextoraw('019a698a43ea784389bd8d1b6e9a5cfe'),
          '큰 글씨 프로필',
          'large',
          'default',
          0,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a1dbb5379b3b3e690ac935b8d70'),
          hextoraw('019a698a43ea75a7ae59bcec4f0361ac'),
          '고대비 (TTS 사용)',
          'default',
          'high',
          1,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a1dbb537703b1818998589d188e'),
          hextoraw('019a698a43ea7e4aa01065cd58d2ccd8'),
          '최대 설정 (TTS)',
          'very_large',
          'high',
          1,
          current_timestamp
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a1dbb53741e90dacf45f3511607'),
          hextoraw('019a698a43ea7b279103a70f0065184a'),
          '약한 고대비',
          'large',
          'little',
          0,
          current_timestamp
     FROM dual;

COMMIT;