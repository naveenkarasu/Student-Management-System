import { useAuth } from '@/hooks/useAuth';
import { useStudentGrades, useStudentGpa } from '@/hooks/useGrades';
import { Card, CardHeader, CardTitle, CardContent, CardDescription } from '@/components/ui/card';
import { Table, TableHeader, TableBody, TableHead, TableRow, TableCell } from '@/components/ui/table';
import { Badge } from '@/components/ui/badge';
import { Loading } from '@/components/ui/loading';
import { GradeChart3D } from '@/components/three/GradeChart3D';
import { AcademicOrbit } from '@/components/three/AcademicOrbit';
import { demoUsers } from '@/mocks/data';

export function MyGradesPage() {
  const { user } = useAuth();
  const demoUser = demoUsers.find((u) => u.username === user?.username);
  const studentId = demoUser?.linkedId ?? 0;

  const { data: grades, isLoading: loadingGrades } = useStudentGrades(studentId);
  const { data: gpa, isLoading: loadingGpa } = useStudentGpa(studentId);

  if (loadingGrades || loadingGpa) return <Loading />;

  const semesters = grades ? [...new Set(grades.map((g) => g.semester))] : [];

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-primary-950">My Grades</h1>
        <p className="text-primary-600">View your academic performance and grade visualizations.</p>
      </div>

      {gpa && (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card>
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium text-primary-600">Cumulative GPA</CardTitle>
            </CardHeader>
            <CardContent>
              <p className="text-3xl font-bold text-primary-950">{gpa.gpa.toFixed(2)}</p>
            </CardContent>
          </Card>
          <Card>
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium text-primary-600">Total Credits</CardTitle>
            </CardHeader>
            <CardContent>
              <p className="text-3xl font-bold text-primary-950">{gpa.totalCredits}</p>
            </CardContent>
          </Card>
          <Card>
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium text-primary-600">Total Grade Points</CardTitle>
            </CardHeader>
            <CardContent>
              <p className="text-3xl font-bold text-primary-950">{gpa.totalGradePoints.toFixed(1)}</p>
            </CardContent>
          </Card>
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <CardTitle>3D Grade Chart</CardTitle>
            <CardDescription>Visualize your marks as a 3D bar chart. Drag to rotate.</CardDescription>
          </CardHeader>
          <CardContent>
            <GradeChart3D grades={grades ?? []} />
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Academic Orbit</CardTitle>
            <CardDescription>
              Your courses orbiting around you. Size represents grade points, distance represents credits.
              Hover for details.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <AcademicOrbit grades={grades ?? []} />
          </CardContent>
        </Card>
      </div>

      {semesters.map((semester) => {
        const semGrades = grades?.filter((g) => g.semester === semester) ?? [];
        return (
          <Card key={semester}>
            <CardHeader>
              <CardTitle>{semester}</CardTitle>
              <CardDescription>{semGrades.length} course(s)</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Course Code</TableHead>
                    <TableHead>Course Name</TableHead>
                    <TableHead>Credits</TableHead>
                    <TableHead>Marks</TableHead>
                    <TableHead>Grade</TableHead>
                    <TableHead>Grade Points</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {semGrades.map((g) => (
                    <TableRow key={g.id}>
                      <TableCell className="font-medium">{g.courseCode}</TableCell>
                      <TableCell>{g.courseName}</TableCell>
                      <TableCell>{g.credits}</TableCell>
                      <TableCell>{g.marks}</TableCell>
                      <TableCell>
                        <Badge
                          variant={
                            g.letterGrade === 'A'
                              ? 'success'
                              : g.letterGrade === 'F'
                              ? 'destructive'
                              : 'secondary'
                          }
                        >
                          {g.letterGrade}
                        </Badge>
                      </TableCell>
                      <TableCell>{g.gradePoints.toFixed(1)}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        );
      })}

      {(!grades || grades.length === 0) && (
        <Card>
          <CardContent className="py-12 text-center text-primary-500">
            No grades available yet. Check back after your courses are graded.
          </CardContent>
        </Card>
      )}
    </div>
  );
}
