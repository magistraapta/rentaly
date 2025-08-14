ALTER TABLE invoices ADD COLUMN returned_at timestamp;
ALTER TABLE invoices ADD COLUMN rent_status VARCHAR DEFAULT 'not_rented';
