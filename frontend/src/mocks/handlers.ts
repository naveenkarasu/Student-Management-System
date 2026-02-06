import { http, HttpResponse, delay } from 'msw';
import {
  demoUsers,
  mockStudents,
  mockCourses,
  mockEnrollments,
  mockGrades,
  getNextStudentId,
  getNextCourseId,
  getNextEnrollmentId,
  getNextGradeId,
} from './data';
import { getLetterGrade, getGradePoints } from '@/types';

const students = [...mockStudents];
const courses = [...mockCourses];
const enrollments = [...mockEnrollments];
const grades = [...mockGrades];

export const handlers = [
  // Auth - Login
  http.post('*/api/v1/auth/login', async ({ request }) => {
    await delay(300);
    const body = (await request.json()) as { username: string; password: string };
    const user = demoUsers.find(
      (u) => u.username === body.username && u.password === body.password
    );
    if (!user) {
      return HttpResponse.json({ message: 'Invalid credentials' }, { status: 401 });
    }
    return HttpResponse.json({
      token: `demo-jwt-token-${user.role.toLowerCase()}-${Date.now()}`,
      username: user.username,
      role: user.role,
      userId: user.id,
    });
  }),

  // Auth - Register
  http.post('*/api/v1/auth/register', async () => {
    await delay(300);
    return HttpResponse.json({ message: 'User registered successfully' }, { status: 201 });
  }),

  // Students - Get All
  http.get('*/api/v1/students', async () => {
    await delay(200);
    return HttpResponse.json(students);
  }),

  // Students - Get by ID
  http.get('*/api/v1/students/:id', async ({ params }) => {
    await delay(200);
    const student = students.find((s) => s.id === Number(params.id));
    if (!student) {
      return HttpResponse.json({ message: 'Student not found' }, { status: 404 });
    }
    return HttpResponse.json(student);
  }),

  // Students - Create
  http.post('*/api/v1/students', async ({ request }) => {
    await delay(300);
    const body = (await request.json()) as Record<string, unknown>;
    const newStudent = {
      id: getNextStudentId(),
      firstName: body.firstName as string,
      lastName: body.lastName as string,
      email: body.email as string,
      dateOfBirth: body.dateOfBirth as string,
      enrollmentDate: body.enrollmentDate as string,
      major: body.major as string,
    };
    students.push(newStudent);
    return HttpResponse.json(newStudent, { status: 201 });
  }),

  // Students - Update
  http.put('*/api/v1/students/:id', async ({ params, request }) => {
    await delay(300);
    const idx = students.findIndex((s) => s.id === Number(params.id));
    if (idx === -1) {
      return HttpResponse.json({ message: 'Student not found' }, { status: 404 });
    }
    const body = (await request.json()) as Record<string, unknown>;
    students[idx] = { ...students[idx], ...body };
    return HttpResponse.json(students[idx]);
  }),

  // Students - Delete
  http.delete('*/api/v1/students/:id', async ({ params }) => {
    await delay(300);
    const idx = students.findIndex((s) => s.id === Number(params.id));
    if (idx === -1) {
      return HttpResponse.json({ message: 'Student not found' }, { status: 404 });
    }
    students.splice(idx, 1);
    return new HttpResponse(null, { status: 204 });
  }),

  // Courses - Get All
  http.get('*/api/v1/courses', async () => {
    await delay(200);
    return HttpResponse.json(courses);
  }),

  // Courses - Get by ID
  http.get('*/api/v1/courses/:id', async ({ params }) => {
    await delay(200);
    const course = courses.find((c) => c.id === Number(params.id));
    if (!course) {
      return HttpResponse.json({ message: 'Course not found' }, { status: 404 });
    }
    return HttpResponse.json(course);
  }),

  // Courses - Get by Faculty
  http.get('*/api/v1/courses/faculty/:facultyId', async ({ params }) => {
    await delay(200);
    const facultyCourses = courses.filter((c) => c.facultyId === Number(params.facultyId));
    return HttpResponse.json(facultyCourses);
  }),

  // Courses - Create
  http.post('*/api/v1/courses', async ({ request }) => {
    await delay(300);
    const body = (await request.json()) as Record<string, unknown>;
    const newCourse = {
      id: getNextCourseId(),
      courseCode: body.courseCode as string,
      courseName: body.courseName as string,
      credits: body.credits as number,
      facultyId: body.facultyId as number,
      facultyName: 'Dr. Smith',
      semester: body.semester as string,
      maxCapacity: body.maxCapacity as number,
      currentEnrollment: 0,
    };
    courses.push(newCourse);
    return HttpResponse.json(newCourse, { status: 201 });
  }),

  // Courses - Update
  http.put('*/api/v1/courses/:id', async ({ params, request }) => {
    await delay(300);
    const idx = courses.findIndex((c) => c.id === Number(params.id));
    if (idx === -1) {
      return HttpResponse.json({ message: 'Course not found' }, { status: 404 });
    }
    const body = (await request.json()) as Record<string, unknown>;
    courses[idx] = { ...courses[idx], ...body };
    return HttpResponse.json(courses[idx]);
  }),

  // Courses - Delete
  http.delete('*/api/v1/courses/:id', async ({ params }) => {
    await delay(300);
    const idx = courses.findIndex((c) => c.id === Number(params.id));
    if (idx === -1) {
      return HttpResponse.json({ message: 'Course not found' }, { status: 404 });
    }
    courses.splice(idx, 1);
    return new HttpResponse(null, { status: 204 });
  }),

  // Enrollments - Create
  http.post('*/api/v1/enrollments', async ({ request }) => {
    await delay(300);
    const body = (await request.json()) as Record<string, unknown>;
    const student = students.find((s) => s.id === Number(body.studentId));
    const course = courses.find((c) => c.id === Number(body.courseId));
    const newEnrollment = {
      id: getNextEnrollmentId(),
      studentId: body.studentId as number,
      courseId: body.courseId as number,
      semester: body.semester as string,
      enrollmentDate: new Date().toISOString().split('T')[0],
      studentName: student ? `${student.firstName} ${student.lastName}` : 'Unknown',
      courseName: course?.courseName || 'Unknown',
      courseCode: course?.courseCode || 'UNK',
    };
    enrollments.push(newEnrollment);
    return HttpResponse.json(newEnrollment, { status: 201 });
  }),

  // Enrollments - Get by Student
  http.get('*/api/v1/enrollments/student/:studentId', async ({ params }) => {
    await delay(200);
    const studentEnrollments = enrollments.filter(
      (e) => e.studentId === Number(params.studentId)
    );
    return HttpResponse.json(studentEnrollments);
  }),

  // Enrollments - Delete
  http.delete('*/api/v1/enrollments/:id', async ({ params }) => {
    await delay(300);
    const idx = enrollments.findIndex((e) => e.id === Number(params.id));
    if (idx === -1) {
      return HttpResponse.json({ message: 'Enrollment not found' }, { status: 404 });
    }
    enrollments.splice(idx, 1);
    return new HttpResponse(null, { status: 204 });
  }),

  // Grades - Create
  http.post('*/api/v1/grades', async ({ request }) => {
    await delay(300);
    const body = (await request.json()) as Record<string, unknown>;
    const enrollment = enrollments.find((e) => e.id === Number(body.enrollmentId));
    const course = enrollment ? courses.find((c) => c.id === enrollment.courseId) : null;
    const marks = body.marks as number;
    const letterGrade = getLetterGrade(marks);
    const newGrade = {
      id: getNextGradeId(),
      enrollmentId: body.enrollmentId as number,
      studentId: enrollment?.studentId || 0,
      courseId: enrollment?.courseId || 0,
      marks,
      letterGrade,
      gradePoints: getGradePoints(letterGrade),
      courseName: course?.courseName || '',
      courseCode: course?.courseCode || '',
      credits: course?.credits || 3,
      semester: enrollment?.semester || '',
      studentName: enrollment?.studentName,
    };
    grades.push(newGrade);
    return HttpResponse.json(newGrade, { status: 201 });
  }),

  // Grades - Get by Student
  http.get('*/api/v1/grades/student/:studentId', async ({ params }) => {
    await delay(200);
    const studentGrades = grades.filter((g) => g.studentId === Number(params.studentId));
    return HttpResponse.json(studentGrades);
  }),

  // Grades - Get by Semester
  http.get('*/api/v1/grades/student/:studentId/semester/:semester', async ({ params }) => {
    await delay(200);
    const semester = decodeURIComponent(params.semester as string);
    const studentGrades = grades.filter(
      (g) => g.studentId === Number(params.studentId) && g.semester === semester
    );
    return HttpResponse.json(studentGrades);
  }),

  // Grades - Get GPA
  http.get('*/api/v1/grades/student/:studentId/gpa', async ({ params }) => {
    await delay(200);
    const studentGrades = grades.filter((g) => g.studentId === Number(params.studentId));
    if (studentGrades.length === 0) {
      return HttpResponse.json({ gpa: 0, totalCredits: 0, totalGradePoints: 0 });
    }
    let totalCredits = 0;
    let totalGradePoints = 0;
    studentGrades.forEach((g) => {
      totalCredits += g.credits;
      totalGradePoints += g.gradePoints * g.credits;
    });
    return HttpResponse.json({
      gpa: Math.round((totalGradePoints / totalCredits) * 100) / 100,
      totalCredits,
      totalGradePoints: Math.round(totalGradePoints * 100) / 100,
    });
  }),

  // Grades - Get Semester GPA
  http.get('*/api/v1/grades/student/:studentId/semester-gpa/:semester', async ({ params }) => {
    await delay(200);
    const semester = decodeURIComponent(params.semester as string);
    const studentGrades = grades.filter(
      (g) => g.studentId === Number(params.studentId) && g.semester === semester
    );
    if (studentGrades.length === 0) {
      return HttpResponse.json({ gpa: 0, totalCredits: 0, totalGradePoints: 0 });
    }
    let totalCredits = 0;
    let totalGradePoints = 0;
    studentGrades.forEach((g) => {
      totalCredits += g.credits;
      totalGradePoints += g.gradePoints * g.credits;
    });
    return HttpResponse.json({
      gpa: Math.round((totalGradePoints / totalCredits) * 100) / 100,
      totalCredits,
      totalGradePoints: Math.round(totalGradePoints * 100) / 100,
    });
  }),

  // Transcripts - Download PDF
  http.get('*/api/v1/transcripts/:studentId/pdf', async ({ params }) => {
    await delay(500);
    const student = students.find((s) => s.id === Number(params.studentId));
    const studentGrades = grades.filter((g) => g.studentId === Number(params.studentId));

    // Generate a simple text-based "PDF" for demo mode
    const lines = [
      'OFFICIAL ACADEMIC TRANSCRIPT',
      '================================',
      '',
      `Student: ${student ? `${student.firstName} ${student.lastName}` : 'Unknown'}`,
      `Email: ${student?.email || 'N/A'}`,
      `Major: ${student?.major || 'N/A'}`,
      `Enrollment Date: ${student?.enrollmentDate || 'N/A'}`,
      '',
      '--- GRADES ---',
      '',
    ];

    const semesters = [...new Set(studentGrades.map((g) => g.semester))];
    semesters.forEach((sem) => {
      lines.push(`Semester: ${sem}`);
      lines.push('-'.repeat(50));
      const semGrades = studentGrades.filter((g) => g.semester === sem);
      semGrades.forEach((g) => {
        lines.push(`  ${g.courseCode} - ${g.courseName}: ${g.marks} (${g.letterGrade})`);
      });
      lines.push('');
    });

    let totalCredits = 0;
    let totalGradePoints = 0;
    studentGrades.forEach((g) => {
      totalCredits += g.credits;
      totalGradePoints += g.gradePoints * g.credits;
    });
    const gpa = totalCredits > 0 ? (totalGradePoints / totalCredits).toFixed(2) : '0.00';
    lines.push(`Cumulative GPA: ${gpa}`);
    lines.push(`Total Credits: ${totalCredits}`);

    const content = lines.join('\n');
    const blob = new Blob([content], { type: 'text/plain' });

    return new HttpResponse(blob, {
      headers: {
        'Content-Type': 'application/octet-stream',
        'Content-Disposition': `attachment; filename="transcript_${params.studentId}.txt"`,
      },
    });
  }),
];
