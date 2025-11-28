UPDATE users
   SET
   user_role = 'SELLER'
 WHERE users.user_id IN (
   SELECT DISTINCT user_id
     FROM sellers
);
COMMIT;