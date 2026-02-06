-- Student Management System Database Schema
-- MySQL 5.7 Compatible
-- Created: 2018

-- Create database
CREATE DATABASE IF NOT EXISTS student_management_db;
USE student_management_db;

-- Users table for authentication
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'faculty', 'student') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Students table
CREATE TABLE students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE,
    roll_number VARCHAR(20) UNIQUE,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    department VARCHAR(100),
    semester INT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- Faculty table (for storing faculty details)
CREATE TABLE faculty (
    faculty_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    department VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- Courses table
CREATE TABLE courses (
    course_id INT PRIMARY KEY AUTO_INCREMENT,
    course_code VARCHAR(20) UNIQUE,
    course_name VARCHAR(255) NOT NULL,
    credits INT,
    faculty_id INT,
    FOREIGN KEY (faculty_id) REFERENCES faculty(faculty_id) ON DELETE SET NULL
);

-- Enrollments table
CREATE TABLE enrollments (
    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    course_id INT,
    semester VARCHAR(20),
    enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    UNIQUE KEY unique_enrollment (student_id, course_id, semester)
);

-- Grades table
CREATE TABLE grades (
    grade_id INT PRIMARY KEY AUTO_INCREMENT,
    enrollment_id INT UNIQUE,
    marks DECIMAL(5,2),
    grade CHAR(2),
    graded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id) ON DELETE CASCADE
);

-- Insert default admin user (password: admin123)
INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin');

-- Insert sample faculty users
INSERT INTO users (username, password, role) VALUES ('faculty1', 'faculty123', 'faculty');
INSERT INTO users (username, password, role) VALUES ('faculty2', 'faculty123', 'faculty');

-- Insert faculty details
INSERT INTO faculty (user_id, name, email, department) VALUES (2, 'Dr. John Smith', 'john.smith@university.edu', 'Computer Science');
INSERT INTO faculty (user_id, name, email, department) VALUES (3, 'Dr. Jane Doe', 'jane.doe@university.edu', 'Computer Science');

-- Insert sample student users
INSERT INTO users (username, password, role) VALUES ('student1', 'student123', 'student');
INSERT INTO users (username, password, role) VALUES ('student2', 'student123', 'student');

-- Insert student details
INSERT INTO students (user_id, roll_number, name, email, department, semester)
VALUES (4, 'CS2018001', 'Alice Johnson', 'alice.johnson@university.edu', 'Computer Science', 5);
INSERT INTO students (user_id, roll_number, name, email, department, semester)
VALUES (5, 'CS2018002', 'Bob Williams', 'bob.williams@university.edu', 'Computer Science', 5);

-- Insert sample courses
INSERT INTO courses (course_code, course_name, credits, faculty_id) VALUES ('CS101', 'Introduction to Programming', 4, 1);
INSERT INTO courses (course_code, course_name, credits, faculty_id) VALUES ('CS201', 'Data Structures', 4, 1);
INSERT INTO courses (course_code, course_name, credits, faculty_id) VALUES ('CS301', 'Database Systems', 3, 2);
INSERT INTO courses (course_code, course_name, credits, faculty_id) VALUES ('CS401', 'Software Engineering', 3, 2);

-- Insert sample enrollments
INSERT INTO enrollments (student_id, course_id, semester) VALUES (1, 1, 'Fall 2018');
INSERT INTO enrollments (student_id, course_id, semester) VALUES (1, 2, 'Fall 2018');
INSERT INTO enrollments (student_id, course_id, semester) VALUES (1, 3, 'Fall 2018');
INSERT INTO enrollments (student_id, course_id, semester) VALUES (2, 1, 'Fall 2018');
INSERT INTO enrollments (student_id, course_id, semester) VALUES (2, 2, 'Fall 2018');

-- Insert sample grades
INSERT INTO grades (enrollment_id, marks, grade) VALUES (1, 85.50, 'A');
INSERT INTO grades (enrollment_id, marks, grade) VALUES (2, 78.00, 'B+');
INSERT INTO grades (enrollment_id, marks, grade) VALUES (3, 92.00, 'A+');
INSERT INTO grades (enrollment_id, marks, grade) VALUES (4, 72.50, 'B');
INSERT INTO grades (enrollment_id, marks, grade) VALUES (5, 88.00, 'A');
