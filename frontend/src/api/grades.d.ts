import type { Grade, GradeCreateRequest, GpaResponse } from '@/types';
export declare const gradesApi: {
    create: (data: GradeCreateRequest) => Promise<Grade>;
    getByStudent: (studentId: number) => Promise<Grade[]>;
    getBySemester: (studentId: number, semester: string) => Promise<Grade[]>;
    getGpa: (studentId: number) => Promise<GpaResponse>;
    getSemesterGpa: (studentId: number, semester: string) => Promise<GpaResponse>;
};
