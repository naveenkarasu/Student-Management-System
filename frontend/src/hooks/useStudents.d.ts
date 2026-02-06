import type { StudentCreateRequest } from '@/types';
export declare function useStudents(): import("@tanstack/react-query").UseQueryResult<import("@/types").Student[], Error>;
export declare function useStudent(id: number): import("@tanstack/react-query").UseQueryResult<import("@/types").Student, Error>;
export declare function useCreateStudent(): import("@tanstack/react-query").UseMutationResult<import("@/types").Student, Error, StudentCreateRequest, unknown>;
export declare function useUpdateStudent(): import("@tanstack/react-query").UseMutationResult<import("@/types").Student, Error, {
    id: number;
    data: Partial<StudentCreateRequest>;
}, unknown>;
export declare function useDeleteStudent(): import("@tanstack/react-query").UseMutationResult<void, Error, number, unknown>;
