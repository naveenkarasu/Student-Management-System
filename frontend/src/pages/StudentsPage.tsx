import { useState } from 'react';
import { useStudents, useCreateStudent, useUpdateStudent, useDeleteStudent } from '@/hooks/useStudents';
import { StudentForm } from '@/components/students/StudentForm';
import { Button } from '@/components/ui/button';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { Table, TableHeader, TableBody, TableHead, TableRow, TableCell } from '@/components/ui/table';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Badge } from '@/components/ui/badge';
import { Loading } from '@/components/ui/loading';
import { useToast } from '@/components/ui/toast';
import { Plus, Pencil, Trash2 } from 'lucide-react';
import type { Student, StudentCreateRequest } from '@/types';

export function StudentsPage() {
  const { data: students, isLoading } = useStudents();
  const createStudent = useCreateStudent();
  const updateStudent = useUpdateStudent();
  const deleteStudent = useDeleteStudent();
  const { addToast } = useToast();

  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingStudent, setEditingStudent] = useState<Student | null>(null);

  const handleCreate = (data: StudentCreateRequest) => {
    createStudent.mutate(data, {
      onSuccess: () => {
        setDialogOpen(false);
        addToast({ title: 'Student created successfully.', variant: 'success' });
      },
      onError: () => {
        addToast({ title: 'Failed to create student.', variant: 'destructive' });
      },
    });
  };

  const handleUpdate = (data: StudentCreateRequest) => {
    if (!editingStudent) return;
    updateStudent.mutate(
      { id: editingStudent.id, data },
      {
        onSuccess: () => {
          setDialogOpen(false);
          setEditingStudent(null);
          addToast({ title: 'Student updated successfully.', variant: 'success' });
        },
        onError: () => {
          addToast({ title: 'Failed to update student.', variant: 'destructive' });
        },
      }
    );
  };

  const handleDelete = (id: number) => {
    if (!window.confirm('Are you sure you want to delete this student?')) return;
    deleteStudent.mutate(id, {
      onSuccess: () => {
        addToast({ title: 'Student deleted successfully.', variant: 'success' });
      },
      onError: () => {
        addToast({ title: 'Failed to delete student.', variant: 'destructive' });
      },
    });
  };

  const openCreate = () => {
    setEditingStudent(null);
    setDialogOpen(true);
  };

  const openEdit = (student: Student) => {
    setEditingStudent(student);
    setDialogOpen(true);
  };

  if (isLoading) return <Loading />;

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-primary-950">Students</h1>
          <p className="text-primary-600">Manage student records.</p>
        </div>
        <Button onClick={openCreate}>
          <Plus className="h-4 w-4 mr-2" />
          Add Student
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>All Students</CardTitle>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Name</TableHead>
                <TableHead>Email</TableHead>
                <TableHead>Major</TableHead>
                <TableHead>Enrollment Date</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {students?.map((student) => (
                <TableRow key={student.id}>
                  <TableCell className="font-medium">
                    {student.firstName} {student.lastName}
                  </TableCell>
                  <TableCell>{student.email}</TableCell>
                  <TableCell>
                    <Badge variant="secondary">{student.major}</Badge>
                  </TableCell>
                  <TableCell>{student.enrollmentDate}</TableCell>
                  <TableCell className="text-right">
                    <div className="flex justify-end gap-2">
                      <Button variant="ghost" size="sm" onClick={() => openEdit(student)}>
                        <Pencil className="h-4 w-4" />
                      </Button>
                      <Button variant="ghost" size="sm" onClick={() => handleDelete(student.id)}>
                        <Trash2 className="h-4 w-4 text-red-500" />
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))}
              {(!students || students.length === 0) && (
                <TableRow>
                  <TableCell colSpan={5} className="text-center text-primary-500">
                    No students found.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
        <DialogContent onClose={() => setDialogOpen(false)}>
          <DialogHeader>
            <DialogTitle>{editingStudent ? 'Edit Student' : 'Add New Student'}</DialogTitle>
          </DialogHeader>
          <StudentForm
            student={editingStudent}
            onSubmit={editingStudent ? handleUpdate : handleCreate}
            onCancel={() => setDialogOpen(false)}
            isLoading={createStudent.isPending || updateStudent.isPending}
          />
        </DialogContent>
      </Dialog>
    </div>
  );
}
