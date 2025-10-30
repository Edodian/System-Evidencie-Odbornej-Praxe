-- Create databases
CREATE DATABASE IF NOT EXISTS sep_dev;
CREATE DATABASE IF NOT EXISTS sep;

-- Grant privileges
GRANT ALL PRIVILEGES ON sep_dev.* TO 'appuser'@'%';
GRANT ALL PRIVILEGES ON sep.* TO 'appuser'@'%';
FLUSH PRIVILEGES;