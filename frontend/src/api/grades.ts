import apiClient from './client';
import type { Grade, GradeCreateRequest, GpaResponse } from '@/types';

export const gradesApi = {
  create: async (data: GradeCreateRequest): Promise<Grade> => {
    const response = await apiClient.post<Grade>('/api/v1/grades', data);
    return response.data;
  },

  getByStudent: async (studentId: number): Promise<Grade[]> => {
    const response = await apiClient.get<Grade[]>(`/api/v1/grades/student/${studentId}`);
    return response.data;
  },

  getBySemester: async (studentId: number, semester: string): Promise<Grade[]> => {
    const response = await apiClient.get<Grade[]>(
      `/api/v1/grades/student/${studentId}/semester/${semester}`
    );
    return response.data;
  },

  getGpa: async (studentId: number): Promise<GpaResponse> => {
    const response = await apiClient.get<GpaResponse>(`/api/v1/grades/student/${studentId}/gpa`);
    return response.data;
  },

  getSemesterGpa: async (studentId: number, semester: string): Promise<GpaResponse> => {
    const response = await apiClient.get<GpaResponse>(
      `/api/v1/grades/student/${studentId}/semester-gpa/${semester}`
    );
    return response.data;
  },
};
