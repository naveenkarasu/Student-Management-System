import apiClient from './client';
import type { Enrollment, EnrollmentCreateRequest } from '@/types';

export const enrollmentsApi = {
  create: async (data: EnrollmentCreateRequest): Promise<Enrollment> => {
    const response = await apiClient.post<Enrollment>('/api/v1/enrollments', data);
    return response.data;
  },

  getByStudent: async (studentId: number): Promise<Enrollment[]> => {
    const response = await apiClient.get<Enrollment[]>(`/api/v1/enrollments/student/${studentId}`);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/api/v1/enrollments/${id}`);
  },
};
