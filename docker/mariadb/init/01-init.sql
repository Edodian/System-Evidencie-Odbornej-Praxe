-- Create databases
CREATE DATABASE IF NOT EXISTS sep_dev;
CREATE DATABASE IF NOT EXISTS sep;

-- Drop user if exists, then create
DROP USER 'appuser'@'%';
CREATE USER 'appuser'@'%' IDENTIFIED BY 'apppassword';

-- Grant privileges
GRANT ALL PRIVILEGES ON sep_dev.* TO 'appuser'@'%';
GRANT ALL PRIVILEGES ON sep.* TO 'appuser'@'%';
FLUSH PRIVILEGES;

USE sep_dev;

-- User table
CREATE TABLE IF NOT EXISTS user (
  id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  pwd VARCHAR(255),
  tempPwd VARCHAR(255),
  name VARCHAR(255),
  surname VARCHAR(255),
  role VARCHAR(50),
  updatedAt DATETIME
);

-- Table of organizations
CREATE TABLE IF NOT EXISTS organization (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255),
  verified BOOLEAN DEFAULT FALSE
);

-- Test user
INSERT INTO user (email, pwd, name, surname, role, updatedAt)
VALUES ('test@company.com', 'test123', 'Test', 'User', 'STUDENT', NOW());
