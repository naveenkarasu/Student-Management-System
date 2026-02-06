import apiClient from './client';
import type { Student, StudentCreateRequest } from '@/types';

export const studentsApi = {
  getAll: async (): Promise<Student[]> => {
    const response = await apiClient.get<Student[]>('/api/v1/students');
    return response.data;
  },

  getById: async (id: number): Promise<Student> => {
    const response = await apiClient.get<Student>(`/api/v1/students/${id}`);
    return response.data;
  },

  create: async (data: StudentCreateRequest): Promise<Student> => {
    const response = await apiClient.post<Student>('/api/v1/students', data);
    return response.data;
  },

  update: async (id: number, data: Partial<StudentCreateRequest>): Promise<Student> => {
    const response = await apiClient.put<Student>(`/api/v1/students/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/api/v1/students/${id}`);
  },
};
