CREATE TABLE product_ai_summary (
   product_id    RAW(16) PRIMARY KEY,
   summary_text  CLOB,
   usage_context CLOB,
   usage_method  CLOB,
   generated_at  TIMESTAMP DEFAULT current_timestamp
);