-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS sep;

-- Grant all privileges to appuser
GRANT ALL PRIVILEGES ON sep.* TO 'appuser'@'%';
FLUSH PRIVILEGES;