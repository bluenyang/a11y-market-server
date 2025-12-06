UPDATE sellers
   SET
   seller_grade = 'NEWER'
 WHERE seller_grade = 'CERTIFIED'
    OR seller_grade = 'BRONZE';

UPDATE sellers
   SET
   seller_grade = 'REGULAR'
 WHERE seller_grade = 'SUPERIORITY'
    OR seller_grade = 'SILVER';

COMMIT;