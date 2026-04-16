-- update total_price column in orders table based on order_items
UPDATE orders o
SET total_price = sub.calculated_total_price
FROM (SELECT oi.order_id,
             SUM(oi.product_quantity * oi.product_price) AS calculated_total_price
      FROM order_items oi
      GROUP BY oi.order_id) sub
WHERE o.order_id = sub.order_id;
COMMIT;