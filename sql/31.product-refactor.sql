UPDATE sellers
   SET
   seller_grade = 'NEWER'
 WHERE seller_grade = 'CERTIFIED';

UPDATE sellers
   SET
   seller_grade = 'REGULAR'
 WHERE seller_grade = 'SUPERIORITY';

COMMIT;