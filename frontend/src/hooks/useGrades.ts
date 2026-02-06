import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { gradesApi } from '@/api/grades';
import type { GradeCreateRequest } from '@/types';

export function useStudentGrades(studentId: number) {
  return useQuery({
    queryKey: ['grades', 'student', studentId],
    queryFn: () => gradesApi.getByStudent(studentId),
    enabled: studentId > 0,
  });
}

export function useSemesterGrades(studentId: number, semester: string) {
  return useQuery({
    queryKey: ['grades', 'student', studentId, 'semester', semester],
    queryFn: () => gradesApi.getBySemester(studentId, semester),
    enabled: studentId > 0 && semester.length > 0,
  });
}

export function useStudentGpa(studentId: number) {
  return useQuery({
    queryKey: ['grades', 'gpa', studentId],
    queryFn: () => gradesApi.getGpa(studentId),
    enabled: studentId > 0,
  });
}

export function useSemesterGpa(studentId: number, semester: string) {
  return useQuery({
    queryKey: ['grades', 'semester-gpa', studentId, semester],
    queryFn: () => gradesApi.getSemesterGpa(studentId, semester),
    enabled: studentId > 0 && semester.length > 0,
  });
}

export function useCreateGrade() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: GradeCreateRequest) => gradesApi.create(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['grades'] });
    },
  });
}
