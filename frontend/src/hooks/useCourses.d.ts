import type { CourseCreateRequest } from '@/types';
export declare function useCourses(): import("@tanstack/react-query").UseQueryResult<import("@/types").Course[], Error>;
export declare function useCourse(id: number): import("@tanstack/react-query").UseQueryResult<import("@/types").Course, Error>;
export declare function useFacultyCourses(facultyId: number): import("@tanstack/react-query").UseQueryResult<import("@/types").Course[], Error>;
export declare function useCreateCourse(): import("@tanstack/react-query").UseMutationResult<import("@/types").Course, Error, CourseCreateRequest, unknown>;
export declare function useUpdateCourse(): import("@tanstack/react-query").UseMutationResult<import("@/types").Course, Error, {
    id: number;
    data: Partial<CourseCreateRequest>;
}, unknown>;
export declare function useDeleteCourse(): import("@tanstack/react-query").UseMutationResult<void, Error, number, unknown>;
