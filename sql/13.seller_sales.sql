CREATE OR REPLACE VIEW seller_sales_view AS
   SELECT s.seller_id,
    
    -- 총 매출액 (구매확정 건만)
          nvl(
             sum(
                CASE
                   WHEN oi.order_item_status = 'CONFIRMED' THEN
                      oi.product_price * oi.product_quantity
                   ELSE
                      0
                END
             ),
             0
          ) AS total_sales,
    
    -- 총 주문 건수 (구매확정 건이 1개 이상 포함된 고유 주문 수)
          COUNT(DISTINCT
             CASE
                WHEN oi.order_item_status = 'CONFIRMED' THEN
                   oi.order_id
             END
          ) AS total_orders,
    
    -- 총 판매 수량 (구매확정 건만)
          nvl(
             sum(
                CASE
                   WHEN oi.order_item_status = 'CONFIRMED' THEN
                      oi.product_quantity
                   ELSE
                      0
                END
             ),
             0
          ) AS total_products_sold,
             
    -- 총 취소 수량 (취소완료 건만)
          nvl(
             sum(
                CASE
                   WHEN oi.order_item_status = 'CANCELED' THEN
                      oi.product_quantity
                   ELSE
                      0
                END
             ),
             0
          ) AS total_cancelled
     FROM sellers s
     LEFT JOIN products p
   ON s.seller_id = p.seller_id
     LEFT JOIN order_items oi
   ON p.product_id = oi.product_id
    GROUP BY s.seller_id;