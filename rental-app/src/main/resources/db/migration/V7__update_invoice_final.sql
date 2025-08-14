-- Final invoice table updates
-- Create payment_status enum type
CREATE TYPE payment_status AS ENUM ('pending', 'paid', 'cancelled');

-- Create rent_status enum type  
CREATE TYPE rent_status AS ENUM ('rented', 'returned', 'not_rented', 'cancelled');

-- Add payment_status column to invoices table
ALTER TABLE invoices ADD COLUMN payment_status payment_status DEFAULT 'pending';

-- Convert rent_status from VARCHAR to enum
-- First, create a temporary column with the enum type
ALTER TABLE invoices ADD COLUMN rent_status_new rent_status;

-- Update the new column with converted values from the old column
UPDATE invoices SET rent_status_new = 
    CASE 
        WHEN rent_status = 'rented' THEN 'rented'::rent_status
        WHEN rent_status = 'returned' THEN 'returned'::rent_status  
        WHEN rent_status = 'cancelled' THEN 'cancelled'::rent_status
        ELSE 'not_rented'::rent_status
    END;

-- Drop the old column and rename the new one
ALTER TABLE invoices DROP COLUMN rent_status;
ALTER TABLE invoices RENAME COLUMN rent_status_new TO rent_status;

-- Set default value for the enum column
ALTER TABLE invoices ALTER COLUMN rent_status SET DEFAULT 'not_rented';

-- Drop the old invoice_status column if it exists
ALTER TABLE invoices DROP COLUMN IF EXISTS invoice_status;

-- Drop the old rental_status column if it exists
ALTER TABLE invoices DROP COLUMN IF EXISTS rental_status;

-- Drop old enum types if they exist
DROP TYPE IF EXISTS invoice_status;
DROP TYPE IF EXISTS rental_status;
