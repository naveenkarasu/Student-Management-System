import type { Student, Course, Enrollment, Grade } from '@/types';
import { getLetterGrade, getGradePoints } from '@/types';

// Demo accounts
export const demoUsers = [
  { id: 1, username: 'admin', password: 'admin123', role: 'ADMIN' as const, linkedId: null },
  { id: 2, username: 'faculty1', password: 'faculty123', role: 'FACULTY' as const, linkedId: 1 },
  { id: 3, username: 'student1', password: 'student123', role: 'STUDENT' as const, linkedId: 1 },
  { id: 4, username: 'faculty2', password: 'faculty123', role: 'FACULTY' as const, linkedId: 2 },
  { id: 5, username: 'student2', password: 'student123', role: 'STUDENT' as const, linkedId: 2 },
];

// Students
export const mockStudents: Student[] = [
  {
    id: 1,
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@university.edu',
    dateOfBirth: '2002-05-15',
    enrollmentDate: '2023-08-20',
    major: 'Computer Science',
    userId: 3,
  },
  {
    id: 2,
    firstName: 'Jane',
    lastName: 'Smith',
    email: 'jane.smith@university.edu',
    dateOfBirth: '2001-11-22',
    enrollmentDate: '2023-08-20',
    major: 'Mathematics',
    userId: 5,
  },
  {
    id: 3,
    firstName: 'Mike',
    lastName: 'Johnson',
    email: 'mike.johnson@university.edu',
    dateOfBirth: '2003-02-10',
    enrollmentDate: '2023-08-20',
    major: 'Computer Science',
  },
  {
    id: 4,
    firstName: 'Emily',
    lastName: 'Williams',
    email: 'emily.williams@university.edu',
    dateOfBirth: '2002-09-03',
    enrollmentDate: '2024-01-15',
    major: 'Physics',
  },
  {
    id: 5,
    firstName: 'David',
    lastName: 'Brown',
    email: 'david.brown@university.edu',
    dateOfBirth: '2001-07-28',
    enrollmentDate: '2023-08-20',
    major: 'Computer Science',
  },
  {
    id: 6,
    firstName: 'Sarah',
    lastName: 'Davis',
    email: 'sarah.davis@university.edu',
    dateOfBirth: '2002-12-01',
    enrollmentDate: '2024-01-15',
    major: 'Mathematics',
  },
];

// Courses
export const mockCourses: Course[] = [
  {
    id: 1,
    courseCode: 'CS101',
    courseName: 'Introduction to Computer Science',
    credits: 3,
    facultyId: 1,
    facultyName: 'Dr. Smith',
    semester: 'Fall 2024',
    maxCapacity: 40,
    currentEnrollment: 5,
  },
  {
    id: 2,
    courseCode: 'CS201',
    courseName: 'Data Structures and Algorithms',
    credits: 4,
    facultyId: 1,
    facultyName: 'Dr. Smith',
    semester: 'Fall 2024',
    maxCapacity: 35,
    currentEnrollment: 3,
  },
  {
    id: 3,
    courseCode: 'CS301',
    courseName: 'Database Systems',
    credits: 3,
    facultyId: 1,
    facultyName: 'Dr. Smith',
    semester: 'Spring 2025',
    maxCapacity: 30,
    currentEnrollment: 4,
  },
  {
    id: 4,
    courseCode: 'MATH201',
    courseName: 'Linear Algebra',
    credits: 3,
    facultyId: 2,
    facultyName: 'Dr. Chen',
    semester: 'Fall 2024',
    maxCapacity: 45,
    currentEnrollment: 6,
  },
  {
    id: 5,
    courseCode: 'MATH301',
    courseName: 'Probability and Statistics',
    credits: 3,
    facultyId: 2,
    facultyName: 'Dr. Chen',
    semester: 'Spring 2025',
    maxCapacity: 40,
    currentEnrollment: 3,
  },
  {
    id: 6,
    courseCode: 'CS401',
    courseName: 'Software Engineering',
    credits: 4,
    facultyId: 1,
    facultyName: 'Dr. Smith',
    semester: 'Spring 2025',
    maxCapacity: 25,
    currentEnrollment: 2,
  },
  {
    id: 7,
    courseCode: 'PHY101',
    courseName: 'Physics I: Mechanics',
    credits: 4,
    facultyId: 2,
    facultyName: 'Dr. Chen',
    semester: 'Fall 2024',
    maxCapacity: 50,
    currentEnrollment: 4,
  },
  {
    id: 8,
    courseCode: 'CS501',
    courseName: 'Machine Learning',
    credits: 3,
    facultyId: 1,
    facultyName: 'Dr. Smith',
    semester: 'Spring 2025',
    maxCapacity: 30,
    currentEnrollment: 2,
  },
];

// Enrollments
export const mockEnrollments: Enrollment[] = [
  // John Doe - Fall 2024
  { id: 1, studentId: 1, courseId: 1, semester: 'Fall 2024', enrollmentDate: '2024-08-15', studentName: 'John Doe', courseName: 'Introduction to Computer Science', courseCode: 'CS101' },
  { id: 2, studentId: 1, courseId: 2, semester: 'Fall 2024', enrollmentDate: '2024-08-15', studentName: 'John Doe', courseName: 'Data Structures and Algorithms', courseCode: 'CS201' },
  { id: 3, studentId: 1, courseId: 4, semester: 'Fall 2024', enrollmentDate: '2024-08-15', studentName: 'John Doe', courseName: 'Linear Algebra', courseCode: 'MATH201' },
  // John Doe - Spring 2025
  { id: 4, studentId: 1, courseId: 3, semester: 'Spring 2025', enrollmentDate: '2025-01-10', studentName: 'John Doe', courseName: 'Database Systems', courseCode: 'CS301' },
  { id: 5, studentId: 1, courseId: 5, semester: 'Spring 2025', enrollmentDate: '2025-01-10', studentName: 'John Doe', courseName: 'Probability and Statistics', courseCode: 'MATH301' },
  { id: 6, studentId: 1, courseId: 6, semester: 'Spring 2025', enrollmentDate: '2025-01-10', studentName: 'John Doe', courseName: 'Software Engineering', courseCode: 'CS401' },
  // Jane Smith - Fall 2024
  { id: 7, studentId: 2, courseId: 4, semester: 'Fall 2024', enrollmentDate: '2024-08-15', studentName: 'Jane Smith', courseName: 'Linear Algebra', courseCode: 'MATH201' },
  { id: 8, studentId: 2, courseId: 7, semester: 'Fall 2024', enrollmentDate: '2024-08-15', studentName: 'Jane Smith', courseName: 'Physics I: Mechanics', courseCode: 'PHY101' },
  // Mike Johnson - Fall 2024
  { id: 9, studentId: 3, courseId: 1, semester: 'Fall 2024', enrollmentDate: '2024-08-15', studentName: 'Mike Johnson', courseName: 'Introduction to Computer Science', courseCode: 'CS101' },
  { id: 10, studentId: 3, courseId: 2, semester: 'Fall 2024', enrollmentDate: '2024-08-15', studentName: 'Mike Johnson', courseName: 'Data Structures and Algorithms', courseCode: 'CS201' },
  // Emily Williams
  { id: 11, studentId: 4, courseId: 7, semester: 'Fall 2024', enrollmentDate: '2024-08-15', studentName: 'Emily Williams', courseName: 'Physics I: Mechanics', courseCode: 'PHY101' },
  { id: 12, studentId: 4, courseId: 1, semester: 'Fall 2024', enrollmentDate: '2024-08-15', studentName: 'Emily Williams', courseName: 'Introduction to Computer Science', courseCode: 'CS101' },
  // David Brown
  { id: 13, studentId: 5, courseId: 1, semester: 'Fall 2024', enrollmentDate: '2024-08-15', studentName: 'David Brown', courseName: 'Introduction to Computer Science', courseCode: 'CS101' },
  { id: 14, studentId: 5, courseId: 2, semester: 'Fall 2024', enrollmentDate: '2024-08-15', studentName: 'David Brown', courseName: 'Data Structures and Algorithms', courseCode: 'CS201' },
  // Sarah Davis
  { id: 15, studentId: 6, courseId: 4, semester: 'Fall 2024', enrollmentDate: '2024-08-15', studentName: 'Sarah Davis', courseName: 'Linear Algebra', courseCode: 'MATH201' },
  { id: 16, studentId: 6, courseId: 5, semester: 'Spring 2025', enrollmentDate: '2025-01-10', studentName: 'Sarah Davis', courseName: 'Probability and Statistics', courseCode: 'MATH301' },
];

function makeGrade(id: number, enrollmentId: number, studentId: number, courseId: number, marks: number, semester: string): Grade {
  const enrollment = mockEnrollments.find(e => e.id === enrollmentId);
  const course = mockCourses.find(c => c.id === courseId);
  const letterGrade = getLetterGrade(marks);
  return {
    id,
    enrollmentId,
    studentId,
    courseId,
    marks,
    letterGrade,
    gradePoints: getGradePoints(letterGrade),
    courseName: course?.courseName || '',
    courseCode: course?.courseCode || '',
    credits: course?.credits || 3,
    semester,
    studentName: enrollment?.studentName,
  };
}

// Grades
export const mockGrades: Grade[] = [
  // John Doe - Fall 2024
  makeGrade(1, 1, 1, 1, 92, 'Fall 2024'),   // CS101: A
  makeGrade(2, 2, 1, 2, 85, 'Fall 2024'),   // CS201: B
  makeGrade(3, 3, 1, 4, 78, 'Fall 2024'),   // MATH201: C
  // John Doe - Spring 2025
  makeGrade(4, 4, 1, 3, 88, 'Spring 2025'), // CS301: B
  makeGrade(5, 5, 1, 5, 95, 'Spring 2025'), // MATH301: A
  makeGrade(6, 6, 1, 6, 91, 'Spring 2025'), // CS401: A
  // Jane Smith - Fall 2024
  makeGrade(7, 7, 2, 4, 96, 'Fall 2024'),   // MATH201: A
  makeGrade(8, 8, 2, 7, 82, 'Fall 2024'),   // PHY101: B
  // Mike Johnson - Fall 2024
  makeGrade(9, 9, 3, 1, 72, 'Fall 2024'),   // CS101: C
  makeGrade(10, 10, 3, 2, 65, 'Fall 2024'), // CS201: D
  // Emily Williams
  makeGrade(11, 11, 4, 7, 90, 'Fall 2024'), // PHY101: A
  makeGrade(12, 12, 4, 1, 88, 'Fall 2024'), // CS101: B
  // David Brown
  makeGrade(13, 13, 5, 1, 55, 'Fall 2024'), // CS101: F
  makeGrade(14, 14, 5, 2, 73, 'Fall 2024'), // CS201: C
];

let nextStudentId = 7;
let nextCourseId = 9;
let nextEnrollmentId = 17;
let nextGradeId = 15;

export function getNextStudentId() { return nextStudentId++; }
export function getNextCourseId() { return nextCourseId++; }
export function getNextEnrollmentId() { return nextEnrollmentId++; }
export function getNextGradeId() { return nextGradeId++; }
