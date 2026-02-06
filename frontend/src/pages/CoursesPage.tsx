import { useState } from 'react';
import { useCourses, useCreateCourse, useUpdateCourse, useDeleteCourse } from '@/hooks/useCourses';
import { useAuth } from '@/hooks/useAuth';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { Table, TableHeader, TableBody, TableHead, TableRow, TableCell } from '@/components/ui/table';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Badge } from '@/components/ui/badge';
import { Loading } from '@/components/ui/loading';
import { useToast } from '@/components/ui/toast';
import { Plus, Pencil, Trash2 } from 'lucide-react';
import { CourseGrid3D } from '@/components/three/CourseGrid3D';
import type { Course, CourseCreateRequest } from '@/types';

interface CourseFormProps {
  course?: Course | null;
  onSubmit: (data: CourseCreateRequest) => void;
  onCancel: () => void;
  isLoading?: boolean;
}

function CourseForm({ course, onSubmit, onCancel, isLoading }: CourseFormProps) {
  const [formData, setFormData] = useState<CourseCreateRequest>({
    courseCode: course?.courseCode ?? '',
    courseName: course?.courseName ?? '',
    credits: course?.credits ?? 3,
    facultyId: course?.facultyId ?? 1,
    semester: course?.semester ?? '',
    maxCapacity: course?.maxCapacity ?? 30,
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="courseCode">Course Code</Label>
          <Input
            id="courseCode"
            value={formData.courseCode}
            onChange={(e) => setFormData({ ...formData, courseCode: e.target.value })}
            placeholder="e.g., CS101"
            required
          />
        </div>
        <div className="space-y-2">
          <Label htmlFor="credits">Credits</Label>
          <Input
            id="credits"
            type="number"
            min={1}
            max={6}
            value={formData.credits}
            onChange={(e) => setFormData({ ...formData, credits: Number(e.target.value) })}
            required
          />
        </div>
      </div>
      <div className="space-y-2">
        <Label htmlFor="courseName">Course Name</Label>
        <Input
          id="courseName"
          value={formData.courseName}
          onChange={(e) => setFormData({ ...formData, courseName: e.target.value })}
          placeholder="e.g., Introduction to Computer Science"
          required
        />
      </div>
      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="semester">Semester</Label>
          <Input
            id="semester"
            value={formData.semester}
            onChange={(e) => setFormData({ ...formData, semester: e.target.value })}
            placeholder="e.g., Fall 2024"
            required
          />
        </div>
        <div className="space-y-2">
          <Label htmlFor="maxCapacity">Max Capacity</Label>
          <Input
            id="maxCapacity"
            type="number"
            min={1}
            value={formData.maxCapacity}
            onChange={(e) => setFormData({ ...formData, maxCapacity: Number(e.target.value) })}
            required
          />
        </div>
      </div>
      <div className="space-y-2">
        <Label htmlFor="facultyId">Faculty ID</Label>
        <Input
          id="facultyId"
          type="number"
          min={1}
          value={formData.facultyId}
          onChange={(e) => setFormData({ ...formData, facultyId: Number(e.target.value) })}
          required
        />
      </div>
      <div className="flex justify-end gap-2 pt-4">
        <Button type="button" variant="outline" onClick={onCancel}>
          Cancel
        </Button>
        <Button type="submit" disabled={isLoading}>
          {isLoading ? 'Saving...' : course ? 'Update Course' : 'Create Course'}
        </Button>
      </div>
    </form>
  );
}

export function CoursesPage() {
  const { user } = useAuth();
  const { data: courses, isLoading } = useCourses();
  const createCourse = useCreateCourse();
  const updateCourse = useUpdateCourse();
  const deleteCourse = useDeleteCourse();
  const { addToast } = useToast();

  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingCourse, setEditingCourse] = useState<Course | null>(null);

  const isAdmin = user?.role === 'ADMIN';

  const handleCreate = (data: CourseCreateRequest) => {
    createCourse.mutate(data, {
      onSuccess: () => {
        setDialogOpen(false);
        addToast({ title: 'Course created successfully.', variant: 'success' });
      },
      onError: () => {
        addToast({ title: 'Failed to create course.', variant: 'destructive' });
      },
    });
  };

  const handleUpdate = (data: CourseCreateRequest) => {
    if (!editingCourse) return;
    updateCourse.mutate(
      { id: editingCourse.id, data },
      {
        onSuccess: () => {
          setDialogOpen(false);
          setEditingCourse(null);
          addToast({ title: 'Course updated successfully.', variant: 'success' });
        },
        onError: () => {
          addToast({ title: 'Failed to update course.', variant: 'destructive' });
        },
      }
    );
  };

  const handleDelete = (id: number) => {
    if (!window.confirm('Are you sure you want to delete this course?')) return;
    deleteCourse.mutate(id, {
      onSuccess: () => {
        addToast({ title: 'Course deleted successfully.', variant: 'success' });
      },
      onError: () => {
        addToast({ title: 'Failed to delete course.', variant: 'destructive' });
      },
    });
  };

  const openCreate = () => {
    setEditingCourse(null);
    setDialogOpen(true);
  };

  const openEdit = (course: Course) => {
    setEditingCourse(course);
    setDialogOpen(true);
  };

  if (isLoading) return <Loading />;

  return (
    <div className="space-y-6">
      <CourseGrid3D />

      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-primary-950">Courses</h1>
          <p className="text-primary-600">
            {isAdmin ? 'Manage courses in the system.' : 'View available courses.'}
          </p>
        </div>
        {isAdmin && (
          <Button onClick={openCreate}>
            <Plus className="h-4 w-4 mr-2" />
            Add Course
          </Button>
        )}
      </div>

      <Card>
        <CardHeader>
          <CardTitle>All Courses</CardTitle>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Code</TableHead>
                <TableHead>Name</TableHead>
                <TableHead>Credits</TableHead>
                <TableHead>Semester</TableHead>
                <TableHead>Faculty</TableHead>
                <TableHead>Enrollment</TableHead>
                {isAdmin && <TableHead className="text-right">Actions</TableHead>}
              </TableRow>
            </TableHeader>
            <TableBody>
              {courses?.map((course) => (
                <TableRow key={course.id}>
                  <TableCell className="font-medium">{course.courseCode}</TableCell>
                  <TableCell>{course.courseName}</TableCell>
                  <TableCell>{course.credits}</TableCell>
                  <TableCell>
                    <Badge variant="secondary">{course.semester}</Badge>
                  </TableCell>
                  <TableCell>{course.facultyName ?? `Faculty #${course.facultyId}`}</TableCell>
                  <TableCell>{course.currentEnrollment ?? 0}/{course.maxCapacity}</TableCell>
                  {isAdmin && (
                    <TableCell className="text-right">
                      <div className="flex justify-end gap-2">
                        <Button variant="ghost" size="sm" onClick={() => openEdit(course)}>
                          <Pencil className="h-4 w-4" />
                        </Button>
                        <Button variant="ghost" size="sm" onClick={() => handleDelete(course.id)}>
                          <Trash2 className="h-4 w-4 text-red-500" />
                        </Button>
                      </div>
                    </TableCell>
                  )}
                </TableRow>
              ))}
              {(!courses || courses.length === 0) && (
                <TableRow>
                  <TableCell colSpan={isAdmin ? 7 : 6} className="text-center text-primary-500">
                    No courses found.
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
            <DialogTitle>{editingCourse ? 'Edit Course' : 'Add New Course'}</DialogTitle>
          </DialogHeader>
          <CourseForm
            course={editingCourse}
            onSubmit={editingCourse ? handleUpdate : handleCreate}
            onCancel={() => setDialogOpen(false)}
            isLoading={createCourse.isPending || updateCourse.isPending}
          />
        </DialogContent>
      </Dialog>
    </div>
  );
}
