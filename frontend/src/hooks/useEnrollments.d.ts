import type { EnrollmentCreateRequest } from '@/types';
export declare function useStudentEnrollments(studentId: number): import("@tanstack/react-query").UseQueryResult<import("@/types").Enrollment[], Error>;
export declare function useCreateEnrollment(): import("@tanstack/react-query").UseMutationResult<import("@/types").Enrollment, Error, EnrollmentCreateRequest, unknown>;
export declare function useDeleteEnrollment(): import("@tanstack/react-query").UseMutationResult<void, Error, number, unknown>;
