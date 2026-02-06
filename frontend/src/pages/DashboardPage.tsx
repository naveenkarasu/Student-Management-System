import { useAuth } from '@/hooks/useAuth';
import { useStudents } from '@/hooks/useStudents';
import { useCourses } from '@/hooks/useCourses';
import { useStudentGrades, useStudentGpa } from '@/hooks/useGrades';
import { Card, CardHeader, CardTitle, CardContent, CardDescription } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Loading } from '@/components/ui/loading';
import { Users, BookOpen, GraduationCap, BarChart3 } from 'lucide-react';
import { demoUsers } from '@/mocks/data';

function AdminDashboard() {
  const { data: students, isLoading: loadingStudents } = useStudents();
  const { data: courses, isLoading: loadingCourses } = useCourses();

  if (loadingStudents || loadingCourses) return <Loading />;

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Admin Dashboard</h1>
        <p className="text-gray-600">Overview of the student management system.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Total Students</CardTitle>
            <Users className="h-5 w-5 text-primary-600" />
          </CardHeader>
          <CardContent>
            <p className="text-3xl font-bold text-gray-900">{students?.length ?? 0}</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Total Courses</CardTitle>
            <BookOpen className="h-5 w-5 text-primary-600" />
          </CardHeader>
          <CardContent>
            <p className="text-3xl font-bold text-gray-900">{courses?.length ?? 0}</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Semesters</CardTitle>
            <GraduationCap className="h-5 w-5 text-primary-600" />
          </CardHeader>
          <CardContent>
            <p className="text-3xl font-bold text-gray-900">
              {courses ? new Set(courses.map((c) => c.semester)).size : 0}
            </p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Faculty Members</CardTitle>
            <BarChart3 className="h-5 w-5 text-primary-600" />
          </CardHeader>
          <CardContent>
            <p className="text-3xl font-bold text-gray-900">
              {courses ? new Set(courses.map((c) => c.facultyId)).size : 0}
            </p>
          </CardContent>
        </Card>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <CardTitle>Recent Students</CardTitle>
            <CardDescription>Latest enrolled students.</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {students?.slice(-5).reverse().map((s) => (
                <div key={s.id} className="flex items-center justify-between border-b border-gray-100 pb-2">
                  <div>
                    <p className="text-sm font-medium text-gray-900">{s.firstName} {s.lastName}</p>
                    <p className="text-xs text-gray-500">{s.email}</p>
                  </div>
                  <Badge variant="secondary">{s.major}</Badge>
                </div>
              ))}
              {(!students || students.length === 0) && (
                <p className="text-sm text-gray-500">No students found.</p>
              )}
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Course Overview</CardTitle>
            <CardDescription>Active courses in the system.</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {courses?.slice(0, 5).map((c) => (
                <div key={c.id} className="flex items-center justify-between border-b border-gray-100 pb-2">
                  <div>
                    <p className="text-sm font-medium text-gray-900">{c.courseCode} - {c.courseName}</p>
                    <p className="text-xs text-gray-500">{c.semester} | {c.credits} credits</p>
                  </div>
                  <Badge>{c.currentEnrollment ?? 0}/{c.maxCapacity}</Badge>
                </div>
              ))}
              {(!courses || courses.length === 0) && (
                <p className="text-sm text-gray-500">No courses found.</p>
              )}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}

function FacultyDashboard() {
  const { user } = useAuth();
  const { data: courses, isLoading } = useCourses();

  if (isLoading) return <Loading />;

  const demoUser = demoUsers.find((u) => u.username === user?.username);
  const facultyId = demoUser?.linkedId ?? 0;
  const myCourses = courses?.filter((c) => c.facultyId === facultyId) ?? [];

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Faculty Dashboard</h1>
        <p className="text-gray-600">Welcome, {user?.username}. Here are your assigned courses.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">My Courses</CardTitle>
            <BookOpen className="h-5 w-5 text-primary-600" />
          </CardHeader>
          <CardContent>
            <p className="text-3xl font-bold text-gray-900">{myCourses.length}</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Total Enrollment</CardTitle>
            <Users className="h-5 w-5 text-primary-600" />
          </CardHeader>
          <CardContent>
            <p className="text-3xl font-bold text-gray-900">
              {myCourses.reduce((sum, c) => sum + (c.currentEnrollment ?? 0), 0)}
            </p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Semesters</CardTitle>
            <GraduationCap className="h-5 w-5 text-primary-600" />
          </CardHeader>
          <CardContent>
            <p className="text-3xl font-bold text-gray-900">
              {new Set(myCourses.map((c) => c.semester)).size}
            </p>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>My Courses</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {myCourses.map((c) => (
              <div key={c.id} className="flex items-center justify-between border-b border-gray-100 pb-2">
                <div>
                  <p className="text-sm font-medium text-gray-900">{c.courseCode} - {c.courseName}</p>
                  <p className="text-xs text-gray-500">{c.semester} | {c.credits} credits</p>
                </div>
                <Badge variant="secondary">{c.currentEnrollment ?? 0}/{c.maxCapacity} enrolled</Badge>
              </div>
            ))}
            {myCourses.length === 0 && (
              <p className="text-sm text-gray-500">No courses assigned.</p>
            )}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}

function StudentDashboard() {
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
        <h1 className="text-2xl font-bold text-gray-900">Student Dashboard</h1>
        <p className="text-gray-600">Welcome, {user?.username}. Here is your academic summary.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Cumulative GPA</CardTitle>
            <BarChart3 className="h-5 w-5 text-primary-600" />
          </CardHeader>
          <CardContent>
            <p className="text-3xl font-bold text-gray-900">{gpa?.gpa?.toFixed(2) ?? '0.00'}</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Total Credits</CardTitle>
            <BookOpen className="h-5 w-5 text-primary-600" />
          </CardHeader>
          <CardContent>
            <p className="text-3xl font-bold text-gray-900">{gpa?.totalCredits ?? 0}</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Courses Completed</CardTitle>
            <GraduationCap className="h-5 w-5 text-primary-600" />
          </CardHeader>
          <CardContent>
            <p className="text-3xl font-bold text-gray-900">{grades?.length ?? 0}</p>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Recent Grades</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {grades?.slice(-5).reverse().map((g) => (
              <div key={g.id} className="flex items-center justify-between border-b border-gray-100 pb-2">
                <div>
                  <p className="text-sm font-medium text-gray-900">{g.courseCode} - {g.courseName}</p>
                  <p className="text-xs text-gray-500">{g.semester}</p>
                </div>
                <div className="flex items-center gap-2">
                  <span className="text-sm text-gray-600">{g.marks}%</span>
                  <Badge variant={g.letterGrade === 'A' ? 'success' : g.letterGrade === 'F' ? 'destructive' : 'secondary'}>
                    {g.letterGrade}
                  </Badge>
                </div>
              </div>
            ))}
            {(!grades || grades.length === 0) && (
              <p className="text-sm text-gray-500">No grades available yet.</p>
            )}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}

export function DashboardPage() {
  const { user } = useAuth();

  if (user?.role === 'ADMIN') return <AdminDashboard />;
  if (user?.role === 'FACULTY') return <FacultyDashboard />;
  return <StudentDashboard />;
}
