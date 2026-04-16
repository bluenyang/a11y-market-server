DROP TABLE default_addresses;
ALTER TABLE addresses
    ADD is_default BOOLEAN DEFAULT FALSE NOT NULL;