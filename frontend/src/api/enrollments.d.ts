import type { Enrollment, EnrollmentCreateRequest } from '@/types';
export declare const enrollmentsApi: {
    create: (data: EnrollmentCreateRequest) => Promise<Enrollment>;
    getByStudent: (studentId: number) => Promise<Enrollment[]>;
    delete: (id: number) => Promise<void>;
};
