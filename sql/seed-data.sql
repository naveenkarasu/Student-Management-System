-- Seed Data for Student Management System
-- Run this after schema.sql

USE student_management_db;

-- Users (passwords stored as plain text for testing - NOT for production)
INSERT INTO users (username, password, role) VALUES
('admin', 'admin123', 'admin'),
('faculty1', 'faculty123', 'faculty'),
('faculty2', 'faculty456', 'faculty'),
('student1', 'student123', 'student'),
('student2', 'student456', 'student'),
('student3', 'student789', 'student'),
('student4', 'student101', 'student'),
('student5', 'student202', 'student');

-- Students
INSERT INTO students (user_id, roll_number, name, email, department, semester) VALUES
(4, 'CS2018001', 'Rahul Sharma', 'rahul.sharma@university.edu', 'Computer Science', 4),
(5, 'CS2018002', 'Priya Patel', 'priya.patel@university.edu', 'Computer Science', 4),
(6, 'EC2018001', 'Amit Kumar', 'amit.kumar@university.edu', 'Electronics', 4),
(7, 'ME2018001', 'Sneha Reddy', 'sneha.reddy@university.edu', 'Mechanical', 4),
(8, 'CS2018003', 'Vikram Singh', 'vikram.singh@university.edu', 'Computer Science', 4);

-- Courses
INSERT INTO courses (course_code, course_name, credits, faculty_id) VALUES
('CS301', 'Data Structures and Algorithms', 4, 2),
('CS302', 'Database Management Systems', 4, 2),
('CS303', 'Operating Systems', 3, 3),
('CS304', 'Computer Networks', 3, 3),
('MA201', 'Engineering Mathematics III', 4, NULL),
('EC301', 'Digital Signal Processing', 4, NULL),
('ME301', 'Thermodynamics', 3, NULL);

-- Enrollments (Spring 2018 semester)
INSERT INTO enrollments (student_id, course_id, semester) VALUES
(1, 1, 'Spring 2018'),
(1, 2, 'Spring 2018'),
(1, 3, 'Spring 2018'),
(1, 5, 'Spring 2018'),
(2, 1, 'Spring 2018'),
(2, 2, 'Spring 2018'),
(2, 4, 'Spring 2018'),
(2, 5, 'Spring 2018'),
(3, 6, 'Spring 2018'),
(3, 5, 'Spring 2018'),
(4, 7, 'Spring 2018'),
(4, 5, 'Spring 2018'),
(5, 1, 'Spring 2018'),
(5, 2, 'Spring 2018'),
(5, 3, 'Spring 2018');

-- Grades
INSERT INTO grades (enrollment_id, marks, grade) VALUES
(1, 85.50, 'A'),
(2, 78.00, 'B+'),
(3, 92.00, 'A+'),
(4, 71.50, 'B'),
(5, 88.00, 'A'),
(6, 65.00, 'B'),
(7, 76.50, 'B+'),
(8, 82.00, 'A'),
(9, 70.00, 'B'),
(10, 68.50, 'B'),
(13, 90.00, 'A+'),
(14, 73.00, 'B'),
(15, 81.00, 'A');
