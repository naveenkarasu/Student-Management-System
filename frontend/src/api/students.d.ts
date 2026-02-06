import type { Student, StudentCreateRequest } from '@/types';
export declare const studentsApi: {
    getAll: () => Promise<Student[]>;
    getById: (id: number) => Promise<Student>;
    create: (data: StudentCreateRequest) => Promise<Student>;
    update: (id: number, data: Partial<StudentCreateRequest>) => Promise<Student>;
    delete: (id: number) => Promise<void>;
};
