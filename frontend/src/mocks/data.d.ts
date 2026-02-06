import type { Student, Course, Enrollment, Grade } from '@/types';
export declare const demoUsers: ({
    id: number;
    username: string;
    password: string;
    role: "ADMIN";
    linkedId: null;
} | {
    id: number;
    username: string;
    password: string;
    role: "FACULTY";
    linkedId: number;
} | {
    id: number;
    username: string;
    password: string;
    role: "STUDENT";
    linkedId: number;
})[];
export declare const mockStudents: Student[];
export declare const mockCourses: Course[];
export declare const mockEnrollments: Enrollment[];
export declare const mockGrades: Grade[];
export declare function getNextStudentId(): number;
export declare function getNextCourseId(): number;
export declare function getNextEnrollmentId(): number;
export declare function getNextGradeId(): number;
