CREATE TABLE categories (
   category_id   RAW(16) PRIMARY KEY,
   parent_cat_id RAW(16),
   category_name VARCHAR2(100) NOT NULL,
   CONSTRAINT fk_parent_cat FOREIGN KEY ( parent_cat_id )
      REFERENCES categories ( category_id )
);