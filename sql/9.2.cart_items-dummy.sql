INSERT INTO cart_items (
   cart_item_id,
   product_id,
   cart_id,
   quantity
)
   SELECT hextoraw('019a6a1b753371dcab13625f44b2b822'),
          hextoraw('019a69f5c3c07412bc208e2345ea7203'),
          hextoraw('019a6a19d2127423b7206e30165850bb'),
          1
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a1b753378a8a61a4fcbfd15c717'),
          hextoraw('019a69f5c3c07646b57ef56d0a09cd97'),
          hextoraw('019a6a19d2127423b7206e30165850bb'),
          2
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a1b7533733ea1315f11e19864ea'),
          hextoraw('019a69f5c3c07d838b893fb3ed04228d'),
          hextoraw('019a6a19d2127faf85b9f3fbefd78ef3'),
          1
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a1b753372e4b96606aaa7e0cce6'),
          hextoraw('019a69f5c3c07caaa08e69d8ce1dd7cf'),
          hextoraw('019a6a19d2127faf85b9f3fbefd78ef3'),
          1
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a1b7533708fb393bfa2bb4677d7'),
          hextoraw('019a69f5c3c07cf29f8b69058c4e4b1c'),
          hextoraw('019a6a19d212759d9d2722b79510379f'),
          1
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a1b75337937997ca8a6d375c9b1'),
          hextoraw('019a69f5c3c0712994ed97c51816d0a2'),
          hextoraw('019a6a19d212759d9d2722b79510379f'),
          3
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a1b75337ac59ab42554ca935036'),
          hextoraw('019a69f5c3c078feaff4e093db2e9ae1'),
          hextoraw('019a6a19d21277d0b1e9e0345d18a2d8'),
          1
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a1b75337536aabb7c5b8a2ebb0a'),
          hextoraw('019a69f5c3c07d7aa40bc8bc7dd56c27'),
          hextoraw('019a6a19d2127748b173b5d47a2173b2'),
          1
     FROM dual
   UNION ALL
   SELECT hextoraw('019a6a1b753373b8ae1d5935f460cc78'),
          hextoraw('019a69f5c3c070a08693306c227d5cbe'),
          hextoraw('019a6a19d2127748b173b5d47a2173b2'),
          2
     FROM dual;

COMMIT;