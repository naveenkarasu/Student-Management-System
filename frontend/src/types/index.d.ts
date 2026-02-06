export type Role = 'ADMIN' | 'FACULTY' | 'STUDENT';
export interface User {
    id: number;
    username: string;
    role: Role;
    token: string;
}
export interface LoginRequest {
    username: string;
    password: string;
}
export interface LoginResponse {
    token: string;
    username: string;
    role: Role;
    userId: number;
}
export interface RegisterRequest {
    username: string;
    password: string;
    role: Role;
}
export interface Student {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    dateOfBirth: string;
    enrollmentDate: string;
    major: string;
    userId?: number;
}
export interface StudentCreateRequest {
    firstName: string;
    lastName: string;
    email: string;
    dateOfBirth: string;
    enrollmentDate: string;
    major: string;
}
export interface Course {
    id: number;
    courseCode: string;
    courseName: string;
    credits: number;
    facultyId: number;
    facultyName?: string;
    semester: string;
    maxCapacity: number;
    currentEnrollment?: number;
}
export interface CourseCreateRequest {
    courseCode: string;
    courseName: string;
    credits: number;
    facultyId: number;
    semester: string;
    maxCapacity: number;
}
export interface Enrollment {
    id: number;
    studentId: number;
    courseId: number;
    semester: string;
    enrollmentDate: string;
    studentName?: string;
    courseName?: string;
    courseCode?: string;
}
export interface EnrollmentCreateRequest {
    studentId: number;
    courseId: number;
    semester: string;
}
export interface Grade {
    id: number;
    enrollmentId: number;
    studentId: number;
    courseId: number;
    marks: number;
    letterGrade: string;
    gradePoints: number;
    courseName: string;
    courseCode: string;
    credits: number;
    semester: string;
    studentName?: string;
}
export interface GradeCreateRequest {
    enrollmentId: number;
    marks: number;
}
export interface GpaResponse {
    gpa: number;
    totalCredits: number;
    totalGradePoints: number;
}
export interface SemesterGpa {
    semester: string;
    gpa: number;
    credits: number;
}
export interface DashboardStats {
    totalStudents: number;
    totalCourses: number;
    totalEnrollments: number;
    averageGpa: number;
}
export declare function getLetterGrade(marks: number): string;
export declare function getGradePoints(letterGrade: string): number;
export declare function getGradeColor(letterGrade: string): string;
