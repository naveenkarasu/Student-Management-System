import type { Course, CourseCreateRequest } from '@/types';
export declare const coursesApi: {
    getAll: () => Promise<Course[]>;
    getById: (id: number) => Promise<Course>;
    getByFaculty: (facultyId: number) => Promise<Course[]>;
    create: (data: CourseCreateRequest) => Promise<Course>;
    update: (id: number, data: Partial<CourseCreateRequest>) => Promise<Course>;
    delete: (id: number) => Promise<void>;
};
