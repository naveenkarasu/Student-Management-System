import type { LoginRequest, LoginResponse, RegisterRequest } from '@/types';
export declare const authApi: {
    login: (data: LoginRequest) => Promise<LoginResponse>;
    register: (data: RegisterRequest) => Promise<void>;
};
