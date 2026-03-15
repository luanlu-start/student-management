-- Create StudentManagement Database
-- Run this script in SQL Server Management Studio as 'sa' user

-- Check if database exists and create if not
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'StudentManagement')
BEGIN
    CREATE DATABASE StudentManagement;
    PRINT 'Database StudentManagement created successfully!';
END
ELSE
BEGIN
    PRINT 'Database StudentManagement already exists!';
END

-- Use the database
USE StudentManagement;

-- Optional: Set recovery model
ALTER DATABASE StudentManagement SET RECOVERY SIMPLE;

PRINT 'Database initialization complete!';

