CREATE OR REPLACE VIEW view_seller_top_products AS
   SELECT p.seller_id,
          p.product_id,
          p.product_name,
          p.product_price,
          pi.image_url AS product_image_url,
          COUNT(oi.order_item_id) AS order_count,
          SUM(oi.product_quantity) AS total_quantity_sold,
          SUM(oi.product_price * oi.product_quantity) AS total_sales_amount,
          RANK()
          OVER(PARTITION BY p.seller_id
               ORDER BY SUM(oi.product_price * oi.product_quantity) DESC
          ) AS sales_rank
     FROM products p
     LEFT JOIN product_images pi
   ON p.product_id = pi.product_id
     LEFT JOIN order_items oi
   ON p.product_id = oi.product_id
      AND oi.order_item_status = 'CONFIRMED'
    WHERE p.product_status = 'APPROVED'
    GROUP BY p.seller_id,
             p.product_id,
             p.product_name,
             p.product_price,
             pi.image_url
   HAVING COUNT(oi.order_item_id) > 0
    ORDER BY p.seller_id,
             sales_rank;