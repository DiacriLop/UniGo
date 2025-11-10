-- Add salt column to usuarios table
ALTER TABLE usuarios
ADD COLUMN salt VARCHAR(24) NOT NULL AFTER clave_hash;

-- Update existing users with a default salt (they'll need to reset their passwords)
-- This is just for existing data. New users will get a proper salt during registration.
UPDATE usuarios 
SET salt = 'default_salt_12345' 
WHERE salt IS NULL OR salt = '';
