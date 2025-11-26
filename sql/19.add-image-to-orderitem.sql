-- add product_image_url column to order_items table
ALTER TABLE order_items ADD product_image_url VARCHAR2(255);

-- update existing records to set a default image URL
UPDATE order_items
   SET
   product_image_url = (
      SELECT image_url
        FROM product_images
       WHERE product_id = order_items.product_id
   )
 WHERE product_image_url IS NULL;
COMMIT;