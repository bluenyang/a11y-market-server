INSERT INTO carts (
   cart_id,
   user_id
)
   SELECT hextoraw('019a6a19d2127423b7206e30165850bb'),
          hextoraw('019a698a43ea778587a64ba7e9e58784')
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a19d2127faf85b9f3fbefd78ef3'),
          hextoraw('019a698a43ea7dca8b34cde9a3850adb')
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a19d212759d9d2722b79510379f'),
          hextoraw('019a698a43ea7ba38634d17b296bd88c')
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a19d21277d0b1e9e0345d18a2d8'),
          hextoraw('019a698a43ea784389bd8d1b6e9a5cfe')
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a19d2127748b173b5d47a2173b2'),
          hextoraw('019a698a43ea7f6db7d6b0682abbd378')
     FROM dual;

COMMIT;