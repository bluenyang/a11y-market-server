CREATE OR REPLACE VIEW view_monthly_popular_products AS
   SELECT p.product_id,
          p.product_name,
          p.product_price,
          pi.image_url AS product_image_url,
          p.category_id,
          cat.category_name,
          p.seller_id,
          SUM(oi.product_quantity) AS monthly_sales_volume,
          COUNT(DISTINCT o.order_id) AS monthly_order_count,
          RANK()
          OVER(
              ORDER BY SUM(oi.product_quantity) DESC
          ) AS ranking
     FROM products p
     JOIN order_items oi
   ON p.product_id = oi.product_id
     JOIN orders o
   ON oi.order_id = o.order_id
     JOIN categories cat
   ON p.category_id = cat.category_id
     LEFT JOIN product_images pi
   ON p.product_id = pi.product_id
    WHERE o.created_at >= add_months(
         sysdate,
         -1
      )
      AND o.created_at < sysdate
    GROUP BY p.product_id,
             p.product_name,
             p.product_price,
             pi.image_url,
             p.category_id,
             cat.category_name,
             p.seller_id
    ORDER BY monthly_sales_volume DESC;

CREATE OR REPLACE VIEW view_category_recommendations AS
   WITH category_tree (
      root_id,
      root_name,
      leaf_id
   ) AS (
      SELECT category_id,
             category_name,
             category_id
        FROM categories
       WHERE parent_cat_id IS NULL
      UNION ALL
      SELECT p.root_id,
             p.root_name,
             cat.category_id
        FROM categories cat
        JOIN category_tree p
      ON cat.parent_cat_id = p.leaf_id
   ),ranked_products AS (
      SELECT ct.root_id,
             ct.root_name,
             vp.product_id,
             vp.product_name,
             vp.product_price,
             vp.product_image_url,
             vp.monthly_sales_volume,
             ROW_NUMBER()
             OVER(PARTITION BY ct.root_id
                  ORDER BY vp.monthly_sales_volume DESC,
                           vp.product_id DESC
             ) AS rn
        FROM view_monthly_popular_products vp
        JOIN category_tree ct
      ON vp.category_id = ct.leaf_id
   )
   SELECT *
     FROM ranked_products
    WHERE rn <= 4;