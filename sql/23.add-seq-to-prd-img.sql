ALTER TABLE product_images ADD image_sequence INT;
-- add 1 as the default sequence number for existing records
UPDATE product_images
   SET
   image_sequence = 1
 WHERE image_sequence IS NULL;
COMMIT;

-- add constraint to ensure not null values in image_sequence column
ALTER TABLE product_images MODIFY
   image_sequence NOT NULL;


CREATE UNIQUE INDEX idx_product_image_sequence ON
   product_images (
      product_id,
      image_sequence
   );