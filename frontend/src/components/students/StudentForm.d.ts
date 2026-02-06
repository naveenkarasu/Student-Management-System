import type { Student, StudentCreateRequest } from '@/types';
interface StudentFormProps {
    student?: Student | null;
    onSubmit: (data: StudentCreateRequest) => void;
    onCancel: () => void;
    isLoading?: boolean;
}
export declare function StudentForm({ student, onSubmit, onCancel, isLoading }: StudentFormProps): import("react/jsx-runtime").JSX.Element;
export {};
