import apiClient from './client';
import type { Course, CourseCreateRequest } from '@/types';

export const coursesApi = {
  getAll: async (): Promise<Course[]> => {
    const response = await apiClient.get<Course[]>('/api/v1/courses');
    return response.data;
  },

  getById: async (id: number): Promise<Course> => {
    const response = await apiClient.get<Course>(`/api/v1/courses/${id}`);
    return response.data;
  },

  getByFaculty: async (facultyId: number): Promise<Course[]> => {
    const response = await apiClient.get<Course[]>(`/api/v1/courses/faculty/${facultyId}`);
    return response.data;
  },

  create: async (data: CourseCreateRequest): Promise<Course> => {
    const response = await apiClient.post<Course>('/api/v1/courses', data);
    return response.data;
  },

  update: async (id: number, data: Partial<CourseCreateRequest>): Promise<Course> => {
    const response = await apiClient.put<Course>(`/api/v1/courses/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/api/v1/courses/${id}`);
  },
};
