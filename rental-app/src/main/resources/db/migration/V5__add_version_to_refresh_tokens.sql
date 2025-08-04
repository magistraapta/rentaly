-- Add version column to refresh_tokens table for optimistic locking
ALTER TABLE refresh_tokens ADD COLUMN version bigint DEFAULT 0; 