-- Seed users with BCrypt-encoded passwords
-- All passwords below are BCrypt hashes of "password123"
-- Generated via BCryptPasswordEncoder with default strength (10 rounds)

INSERT INTO users (username, password, role) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN'),
('prof.smith', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'FACULTY'),
('prof.jones', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'FACULTY'),
('john.doe', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'STUDENT'),
('jane.smith', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'STUDENT'),
('bob.wilson', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'STUDENT');

-- Seed faculty
INSERT INTO faculty (user_id, name, email, department) VALUES
((SELECT user_id FROM users WHERE username = 'prof.smith'), 'Dr. Robert Smith', 'r.smith@university.edu', 'Computer Science'),
((SELECT user_id FROM users WHERE username = 'prof.jones'), 'Dr. Sarah Jones', 's.jones@university.edu', 'Mathematics');

-- Seed students
INSERT INTO students (user_id, roll_number, name, email, department, semester) VALUES
((SELECT user_id FROM users WHERE username = 'john.doe'), 'CS2024001', 'John Doe', 'john.doe@university.edu', 'Computer Science', 3),
((SELECT user_id FROM users WHERE username = 'jane.smith'), 'CS2024002', 'Jane Smith', 'jane.smith@university.edu', 'Computer Science', 3),
((SELECT user_id FROM users WHERE username = 'bob.wilson'), 'MT2024001', 'Bob Wilson', 'bob.wilson@university.edu', 'Mathematics', 2);

-- Seed courses
INSERT INTO courses (course_code, course_name, credits, faculty_id) VALUES
('CS101', 'Introduction to Programming', 4, (SELECT faculty_id FROM faculty WHERE name = 'Dr. Robert Smith')),
('CS201', 'Data Structures and Algorithms', 4, (SELECT faculty_id FROM faculty WHERE name = 'Dr. Robert Smith')),
('CS301', 'Database Systems', 3, (SELECT faculty_id FROM faculty WHERE name = 'Dr. Robert Smith')),
('MT101', 'Calculus I', 4, (SELECT faculty_id FROM faculty WHERE name = 'Dr. Sarah Jones')),
('MT201', 'Linear Algebra', 3, (SELECT faculty_id FROM faculty WHERE name = 'Dr. Sarah Jones'));

-- Seed enrollments
INSERT INTO enrollments (student_id, course_id, semester) VALUES
((SELECT student_id FROM students WHERE roll_number = 'CS2024001'), (SELECT course_id FROM courses WHERE course_code = 'CS101'), 'Fall 2024'),
((SELECT student_id FROM students WHERE roll_number = 'CS2024001'), (SELECT course_id FROM courses WHERE course_code = 'MT101'), 'Fall 2024'),
((SELECT student_id FROM students WHERE roll_number = 'CS2024001'), (SELECT course_id FROM courses WHERE course_code = 'CS201'), 'Spring 2025'),
((SELECT student_id FROM students WHERE roll_number = 'CS2024002'), (SELECT course_id FROM courses WHERE course_code = 'CS101'), 'Fall 2024'),
((SELECT student_id FROM students WHERE roll_number = 'CS2024002'), (SELECT course_id FROM courses WHERE course_code = 'CS301'), 'Fall 2024'),
((SELECT student_id FROM students WHERE roll_number = 'MT2024001'), (SELECT course_id FROM courses WHERE course_code = 'MT101'), 'Fall 2024'),
((SELECT student_id FROM students WHERE roll_number = 'MT2024001'), (SELECT course_id FROM courses WHERE course_code = 'MT201'), 'Fall 2024');

-- Seed grades
INSERT INTO grades (enrollment_id, marks, grade) VALUES
((SELECT enrollment_id FROM enrollments WHERE student_id = (SELECT student_id FROM students WHERE roll_number = 'CS2024001') AND course_id = (SELECT course_id FROM courses WHERE course_code = 'CS101')), 92.0, 'A+'),
((SELECT enrollment_id FROM enrollments WHERE student_id = (SELECT student_id FROM students WHERE roll_number = 'CS2024001') AND course_id = (SELECT course_id FROM courses WHERE course_code = 'MT101')), 78.0, 'B+'),
((SELECT enrollment_id FROM enrollments WHERE student_id = (SELECT student_id FROM students WHERE roll_number = 'CS2024002') AND course_id = (SELECT course_id FROM courses WHERE course_code = 'CS101')), 85.0, 'A'),
((SELECT enrollment_id FROM enrollments WHERE student_id = (SELECT student_id FROM students WHERE roll_number = 'CS2024002') AND course_id = (SELECT course_id FROM courses WHERE course_code = 'CS301')), 71.0, 'B'),
((SELECT enrollment_id FROM enrollments WHERE student_id = (SELECT student_id FROM students WHERE roll_number = 'MT2024001') AND course_id = (SELECT course_id FROM courses WHERE course_code = 'MT101')), 58.0, 'C'),
((SELECT enrollment_id FROM enrollments WHERE student_id = (SELECT student_id FROM students WHERE roll_number = 'MT2024001') AND course_id = (SELECT course_id FROM courses WHERE course_code = 'MT201')), 45.0, 'D+');
