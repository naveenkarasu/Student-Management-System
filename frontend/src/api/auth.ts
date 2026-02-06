import apiClient from './client';
import type { LoginRequest, LoginResponse, RegisterRequest } from '@/types';

export const authApi = {
  login: async (data: LoginRequest): Promise<LoginResponse> => {
    const response = await apiClient.post<LoginResponse>('/api/v1/auth/login', data);
    return response.data;
  },

  register: async (data: RegisterRequest): Promise<void> => {
    await apiClient.post('/api/v1/auth/register', data);
  },
};
