DROP TABLE default_addresses;
ALTER TABLE addresses ADD is_default NUMBER(1) DEFAULT 0 NOT NULL;