-- update total_price column in orders table based on order_items
UPDATE orders o
   SET
   total_price = (
      SELECT SUM(oi.product_quantity * oi.product_price)
        FROM order_items oi
       WHERE oi.order_id = o.order_id
   )
 WHERE ROWNUM > 0;
COMMIT;