import apiClient from './client';

export const transcriptsApi = {
  downloadPdf: async (studentId: number): Promise<Blob> => {
    const response = await apiClient.get(`/api/v1/transcripts/${studentId}/pdf`, {
      responseType: 'blob',
    });
    return response.data;
  },
};
