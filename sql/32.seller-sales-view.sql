DROP VIEW seller_sales_view;

CREATE OR REPLACE VIEW view_seller_dashboard_stats AS
   SELECT s.seller_id,
          SUM(
             CASE
                WHEN oi.order_item_status = 'CONFIRMED' THEN
                   oi.product_price * oi.product_quantity
                ELSE
                   0
             END
          ) AS total_revenue,
          COUNT(DISTINCT
             CASE
                WHEN oi.order_item_status != 'REJECTED' THEN
                   oi.order_item_id
             END
          ) AS total_order_count,
          COUNT(
             CASE
                WHEN oi.order_item_status = 'CONFIRMED' THEN
                   1
             END
          ) AS confirmed_count,
          COUNT(
             CASE
                WHEN oi.order_item_status IN('CANCELED',
                                             'RETURNED') THEN
                   1
             END
          ) AS refunded_count
     FROM sellers s
     LEFT JOIN products p
   ON s.seller_id = p.seller_id
     LEFT JOIN order_items oi
   ON p.product_id = oi.product_id
    WHERE p.product_status = 'APPROVED'
    GROUP BY s.seller_id;