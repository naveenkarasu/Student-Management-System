import { useState } from 'react';
import { useAuth } from '@/hooks/useAuth';
import { useCourses } from '@/hooks/useCourses';
import { useStudents } from '@/hooks/useStudents';
import { useCreateGrade } from '@/hooks/useGrades';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Select } from '@/components/ui/select';
import { Card, CardHeader, CardTitle, CardContent, CardDescription } from '@/components/ui/card';
import { Table, TableHeader, TableBody, TableHead, TableRow, TableCell } from '@/components/ui/table';
import { Badge } from '@/components/ui/badge';
import { Loading } from '@/components/ui/loading';
import { useToast } from '@/components/ui/toast';
import { demoUsers } from '@/mocks/data';
import { mockEnrollments } from '@/mocks/data';
import { getLetterGrade } from '@/types';

export function GradeEntryPage() {
  const { user } = useAuth();
  const { data: courses, isLoading: loadingCourses } = useCourses();
  const { data: students, isLoading: loadingStudents } = useStudents();
  const createGrade = useCreateGrade();
  const { addToast } = useToast();

  const [selectedCourseId, setSelectedCourseId] = useState(0);
  const [gradeEntries, setGradeEntries] = useState<Record<number, string>>({});

  const demoUser = demoUsers.find((u) => u.username === user?.username);
  const facultyId = demoUser?.linkedId ?? 0;

  if (loadingCourses || loadingStudents) return <Loading />;

  const facultyCourses = courses?.filter((c) => c.facultyId === facultyId) ?? [];
  const courseOptions = facultyCourses.map((c) => ({
    value: String(c.id),
    label: `${c.courseCode} - ${c.courseName}`,
  }));

  const selectedCourse = courses?.find((c) => c.id === selectedCourseId);

  const courseEnrollments = mockEnrollments.filter(
    (e) => e.courseId === selectedCourseId
  );

  const handleMarksChange = (enrollmentId: number, value: string) => {
    setGradeEntries({ ...gradeEntries, [enrollmentId]: value });
  };

  const handleSubmitGrade = (enrollmentId: number) => {
    const marks = Number(gradeEntries[enrollmentId]);
    if (isNaN(marks) || marks < 0 || marks > 100) {
      addToast({ title: 'Marks must be between 0 and 100.', variant: 'destructive' });
      return;
    }
    createGrade.mutate(
      { enrollmentId, marks },
      {
        onSuccess: () => {
          addToast({ title: 'Grade submitted successfully.', variant: 'success' });
          const updated = { ...gradeEntries };
          delete updated[enrollmentId];
          setGradeEntries(updated);
        },
        onError: () => {
          addToast({ title: 'Failed to submit grade.', variant: 'destructive' });
        },
      }
    );
  };

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Grade Entry</h1>
        <p className="text-gray-600">Select a course and enter marks for enrolled students.</p>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Select Course</CardTitle>
          <CardDescription>Choose one of your assigned courses to enter grades.</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="max-w-md space-y-2">
            <Label>Course</Label>
            <Select
              options={courseOptions}
              placeholder="Select a course..."
              value={String(selectedCourseId)}
              onChange={(e) => setSelectedCourseId(Number(e.target.value))}
            />
          </div>
        </CardContent>
      </Card>

      {selectedCourse && (
        <Card>
          <CardHeader>
            <CardTitle>
              {selectedCourse.courseCode} - {selectedCourse.courseName}
            </CardTitle>
            <CardDescription>
              {selectedCourse.semester} | {selectedCourse.credits} credits
            </CardDescription>
          </CardHeader>
          <CardContent>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Student</TableHead>
                  <TableHead>Enrollment ID</TableHead>
                  <TableHead>Marks (0-100)</TableHead>
                  <TableHead>Letter Grade</TableHead>
                  <TableHead className="text-right">Action</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {courseEnrollments.map((enrollment) => {
                  const student = students?.find((s) => s.id === enrollment.studentId);
                  const marks = gradeEntries[enrollment.id] ?? '';
                  const numMarks = Number(marks);
                  const letterGrade = marks && !isNaN(numMarks) ? getLetterGrade(numMarks) : '-';
                  return (
                    <TableRow key={enrollment.id}>
                      <TableCell className="font-medium">
                        {student
                          ? `${student.firstName} ${student.lastName}`
                          : enrollment.studentName ?? 'Unknown'}
                      </TableCell>
                      <TableCell>{enrollment.id}</TableCell>
                      <TableCell>
                        <Input
                          type="number"
                          min={0}
                          max={100}
                          value={marks}
                          onChange={(e) => handleMarksChange(enrollment.id, e.target.value)}
                          className="w-24"
                          placeholder="0-100"
                        />
                      </TableCell>
                      <TableCell>
                        <Badge
                          variant={
                            letterGrade === 'A'
                              ? 'success'
                              : letterGrade === 'F'
                              ? 'destructive'
                              : 'secondary'
                          }
                        >
                          {letterGrade}
                        </Badge>
                      </TableCell>
                      <TableCell className="text-right">
                        <Button
                          size="sm"
                          disabled={!marks || createGrade.isPending}
                          onClick={() => handleSubmitGrade(enrollment.id)}
                        >
                          Submit
                        </Button>
                      </TableCell>
                    </TableRow>
                  );
                })}
                {courseEnrollments.length === 0 && (
                  <TableRow>
                    <TableCell colSpan={5} className="text-center text-gray-500">
                      No students enrolled in this course.
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      )}
    </div>
  );
}
