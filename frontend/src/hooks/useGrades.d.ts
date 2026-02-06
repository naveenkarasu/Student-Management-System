import type { GradeCreateRequest } from '@/types';
export declare function useStudentGrades(studentId: number): import("@tanstack/react-query").UseQueryResult<import("@/types").Grade[], Error>;
export declare function useSemesterGrades(studentId: number, semester: string): import("@tanstack/react-query").UseQueryResult<import("@/types").Grade[], Error>;
export declare function useStudentGpa(studentId: number): import("@tanstack/react-query").UseQueryResult<import("@/types").GpaResponse, Error>;
export declare function useSemesterGpa(studentId: number, semester: string): import("@tanstack/react-query").UseQueryResult<import("@/types").GpaResponse, Error>;
export declare function useCreateGrade(): import("@tanstack/react-query").UseMutationResult<import("@/types").Grade, Error, GradeCreateRequest, unknown>;
