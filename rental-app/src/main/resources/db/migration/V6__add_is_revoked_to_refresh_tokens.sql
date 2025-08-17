-- Add is_revoked column to refresh_tokens table
ALTER TABLE refresh_tokens ADD COLUMN is_revoked boolean DEFAULT false NOT NULL;
