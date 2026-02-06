import { useState } from 'react';
import { useStudents } from '@/hooks/useStudents';
import { useCourses } from '@/hooks/useCourses';
import { useStudentEnrollments, useCreateEnrollment, useDeleteEnrollment } from '@/hooks/useEnrollments';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Select } from '@/components/ui/select';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { Table, TableHeader, TableBody, TableHead, TableRow, TableCell } from '@/components/ui/table';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Badge } from '@/components/ui/badge';
import { Loading } from '@/components/ui/loading';
import { useToast } from '@/components/ui/toast';
import { Plus, Trash2, Search } from 'lucide-react';
import { EnrollmentFlow3D } from '@/components/three/EnrollmentFlow3D';

export function EnrollmentsPage() {
  const { data: students, isLoading: loadingStudents } = useStudents();
  const { data: courses, isLoading: loadingCourses } = useCourses();
  const { addToast } = useToast();
  const createEnrollment = useCreateEnrollment();
  const deleteEnrollment = useDeleteEnrollment();

  const [selectedStudentId, setSelectedStudentId] = useState(0);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [newEnrollment, setNewEnrollment] = useState({
    studentId: 0,
    courseId: 0,
    semester: '',
  });

  const { data: enrollments, isLoading: loadingEnrollments } = useStudentEnrollments(selectedStudentId);

  const handleCreate = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newEnrollment.studentId || !newEnrollment.courseId || !newEnrollment.semester) {
      addToast({ title: 'Please fill in all fields.', variant: 'destructive' });
      return;
    }
    createEnrollment.mutate(newEnrollment, {
      onSuccess: () => {
        setDialogOpen(false);
        setNewEnrollment({ studentId: 0, courseId: 0, semester: '' });
        addToast({ title: 'Enrollment created successfully.', variant: 'success' });
      },
      onError: () => {
        addToast({ title: 'Failed to create enrollment.', variant: 'destructive' });
      },
    });
  };

  const handleDelete = (id: number) => {
    if (!window.confirm('Are you sure you want to remove this enrollment?')) return;
    deleteEnrollment.mutate(id, {
      onSuccess: () => {
        addToast({ title: 'Enrollment removed successfully.', variant: 'success' });
      },
      onError: () => {
        addToast({ title: 'Failed to remove enrollment.', variant: 'destructive' });
      },
    });
  };

  if (loadingStudents || loadingCourses) return <Loading />;

  const studentOptions = (students ?? []).map((s) => ({
    value: String(s.id),
    label: `${s.firstName} ${s.lastName}`,
  }));

  const courseOptions = (courses ?? []).map((c) => ({
    value: String(c.id),
    label: `${c.courseCode} - ${c.courseName}`,
  }));

  const semesterOptions = courses
    ? [...new Set(courses.map((c) => c.semester))].map((s) => ({ value: s, label: s }))
    : [];

  return (
    <div className="space-y-6">
      <EnrollmentFlow3D />

      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-primary-950">Enrollments</h1>
          <p className="text-primary-600">Manage student course enrollments.</p>
        </div>
        <Button onClick={() => setDialogOpen(true)}>
          <Plus className="h-4 w-4 mr-2" />
          New Enrollment
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Lookup Student Enrollments</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex items-end gap-4">
            <div className="flex-1 space-y-2">
              <Label>Select Student</Label>
              <Select
                options={studentOptions}
                placeholder="Choose a student..."
                value={String(selectedStudentId)}
                onChange={(e) => setSelectedStudentId(Number(e.target.value))}
              />
            </div>
            <Button
              variant="outline"
              disabled={!selectedStudentId}
              onClick={() => {}}
            >
              <Search className="h-4 w-4 mr-2" />
              View
            </Button>
          </div>
        </CardContent>
      </Card>

      {selectedStudentId > 0 && (
        <Card>
          <CardHeader>
            <CardTitle>
              Enrollments for{' '}
              {students?.find((s) => s.id === selectedStudentId)
                ? `${students.find((s) => s.id === selectedStudentId)!.firstName} ${students.find((s) => s.id === selectedStudentId)!.lastName}`
                : 'Student'}
            </CardTitle>
          </CardHeader>
          <CardContent>
            {loadingEnrollments ? (
              <Loading />
            ) : (
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Course Code</TableHead>
                    <TableHead>Course Name</TableHead>
                    <TableHead>Semester</TableHead>
                    <TableHead>Enrollment Date</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {enrollments?.map((enrollment) => (
                    <TableRow key={enrollment.id}>
                      <TableCell className="font-medium">{enrollment.courseCode ?? '-'}</TableCell>
                      <TableCell>{enrollment.courseName ?? '-'}</TableCell>
                      <TableCell>
                        <Badge variant="secondary">{enrollment.semester}</Badge>
                      </TableCell>
                      <TableCell>{enrollment.enrollmentDate}</TableCell>
                      <TableCell className="text-right">
                        <Button variant="ghost" size="sm" onClick={() => handleDelete(enrollment.id)}>
                          <Trash2 className="h-4 w-4 text-red-500" />
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))}
                  {(!enrollments || enrollments.length === 0) && (
                    <TableRow>
                      <TableCell colSpan={5} className="text-center text-primary-500">
                        No enrollments found for this student.
                      </TableCell>
                    </TableRow>
                  )}
                </TableBody>
              </Table>
            )}
          </CardContent>
        </Card>
      )}

      <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
        <DialogContent onClose={() => setDialogOpen(false)}>
          <DialogHeader>
            <DialogTitle>Create New Enrollment</DialogTitle>
          </DialogHeader>
          <form onSubmit={handleCreate} className="space-y-4">
            <div className="space-y-2">
              <Label>Student</Label>
              <Select
                options={studentOptions}
                placeholder="Select student..."
                value={String(newEnrollment.studentId)}
                onChange={(e) =>
                  setNewEnrollment({ ...newEnrollment, studentId: Number(e.target.value) })
                }
              />
            </div>
            <div className="space-y-2">
              <Label>Course</Label>
              <Select
                options={courseOptions}
                placeholder="Select course..."
                value={String(newEnrollment.courseId)}
                onChange={(e) =>
                  setNewEnrollment({ ...newEnrollment, courseId: Number(e.target.value) })
                }
              />
            </div>
            <div className="space-y-2">
              <Label>Semester</Label>
              <Input
                value={newEnrollment.semester}
                onChange={(e) =>
                  setNewEnrollment({ ...newEnrollment, semester: e.target.value })
                }
                placeholder="e.g., Fall 2024"
                required
              />
            </div>
            <div className="flex justify-end gap-2 pt-4">
              <Button type="button" variant="outline" onClick={() => setDialogOpen(false)}>
                Cancel
              </Button>
              <Button type="submit" disabled={createEnrollment.isPending}>
                {createEnrollment.isPending ? 'Creating...' : 'Create Enrollment'}
              </Button>
            </div>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
}
