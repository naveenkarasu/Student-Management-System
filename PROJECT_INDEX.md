# Project Index: Student Management System

Generated: 2026-02-06

## Project Structure

```
student-management-system/
├── docker-compose.yml               # MySQL (3308) + Backend (8082) + Frontend (3002)
├── README.md
├── backend/
│   ├── pom.xml                      # Spring Boot 3.2.5 (Java 17)
│   ├── Dockerfile                   # temurin:17-jdk -> temurin:17-jre
│   ├── src/main/java/com/studentmgmt/
│   │   ├── StudentManagementApplication.java  # Entry point (port 8082)
│   │   ├── config/
│   │   │   ├── SecurityConfig.java  # JWT + role-based (ADMIN/FACULTY/STUDENT)
│   │   │   ├── JwtAuthFilter.java   # JWT authentication filter
│   │   │   ├── JwtUtil.java         # Token generation/validation
│   │   │   └── OpenApiConfig.java   # Swagger/OpenAPI 3.0
│   │   ├── controller/
│   │   │   ├── AuthController.java      # /api/v1/auth/**
│   │   │   ├── StudentController.java   # /api/v1/students/**
│   │   │   ├── CourseController.java    # /api/v1/courses/**
│   │   │   ├── EnrollmentController.java # /api/v1/enrollments/**
│   │   │   ├── GradeController.java     # /api/v1/grades/**
│   │   │   └── TranscriptController.java # /api/v1/transcripts/**
│   │   ├── model/
│   │   │   ├── User.java            # username, password, role (ADMIN/FACULTY/STUDENT)
│   │   │   ├── Student.java         # studentId, firstName, lastName, email, major, year
│   │   │   ├── Faculty.java         # facultyId, name, email, department
│   │   │   ├── Course.java          # courseId, courseCode, name, credits, faculty
│   │   │   ├── Enrollment.java      # student, course, semester, status
│   │   │   └── Grade.java           # enrollment, marks, letterGrade, gradePoints
│   │   ├── repository/
│   │   │   ├── UserRepository.java
│   │   │   ├── StudentRepository.java
│   │   │   ├── FacultyRepository.java
│   │   │   ├── CourseRepository.java
│   │   │   ├── EnrollmentRepository.java
│   │   │   └── GradeRepository.java    # Custom JPQL: findByStudentId, findByStudentIdAndSemester
│   │   ├── service/
│   │   │   ├── AuthService.java         # Login + JWT, register
│   │   │   ├── StudentService.java      # CRUD + search
│   │   │   ├── CourseService.java       # CRUD + faculty assignment
│   │   │   ├── EnrollmentService.java   # Enroll/drop + duplicate check
│   │   │   ├── GradeService.java        # Grade entry + GPA calculation
│   │   │   └── TranscriptService.java   # PDF generation (OpenPDF)
│   │   └── dto/
│   │       ├── LoginRequest.java, LoginResponse.java
│   │       ├── StudentDto.java, CourseDto.java
│   │       ├── EnrollmentRequest.java
│   │       ├── GradeRequest.java, GradeResponse.java
│   │       └── TranscriptData.java
│   ├── src/main/resources/
│   │   ├── application.properties   # Port 8082, MySQL student_db
│   │   └── db/migration/
│   │       ├── V1__initial_schema.sql  # users, students, faculty, courses, enrollments, grades
│   │       └── V2__seed_data.sql       # Demo data with 3 roles
│   └── src/test/java/com/studentmgmt/
│       └── service/GradeServiceTest.java
├── frontend/
│   ├── package.json                 # React 18.3.1, Vite 5.4.21
│   ├── vite.config.ts              # base: '/Student-Management-System/'
│   ├── tailwind.config.js          # Purple + coral palette
│   ├── Dockerfile                   # Node 20 + Nginx
│   ├── nginx.conf                   # Proxy /api/ -> backend:8082
│   ├── public/mockServiceWorker.js  # MSW service worker
│   └── src/
│       ├── main.tsx                 # React + QueryClient + MSW demo mode
│       ├── App.tsx                  # Routes + role-based rendering
│       ├── pages/
│       │   ├── LoginPage.tsx        # FloatingShapes 3D + demo account buttons
│       │   ├── DashboardPage.tsx    # Role-specific: Admin/Faculty/Student views
│       │   ├── StudentsPage.tsx     # Student CRUD (Admin only)
│       │   ├── CoursesPage.tsx      # Course management
│       │   ├── EnrollmentsPage.tsx  # Enrollment management
│       │   ├── GradeEntryPage.tsx   # Grade entry (Faculty)
│       │   ├── MyGradesPage.tsx     # View grades (Student)
│       │   └── TranscriptPage.tsx   # PDF transcript download
│       ├── components/
│       │   ├── layout/              # Layout, Header, Sidebar
│       │   ├── three/               # GpaGauge3D, FloatingShapes
│       │   └── ui/                  # shadcn: button, input, card, badge, loading, etc.
│       ├── hooks/
│       │   ├── useAuth.ts           # Zustand store with role
│       │   ├── useStudents.ts       # TanStack Query CRUD hooks
│       │   ├── useCourses.ts
│       │   ├── useEnrollments.ts
│       │   └── useGrades.ts         # useStudentGrades, useStudentGpa
│       ├── api/
│       │   ├── client.ts            # Axios + JWT interceptor
│       │   ├── auth.ts, students.ts, courses.ts
│       │   ├── enrollments.ts, grades.ts, transcripts.ts
│       ├── mocks/
│       │   ├── browser.ts           # MSW setup
│       │   ├── handlers.ts          # All API route handlers
│       │   └── data.ts              # Demo users, students, courses, grades
│       ├── types/index.ts
│       └── lib/utils.ts
└── .github/workflows/
    └── deploy-frontend.yml          # GitHub Pages with VITE_DEMO_MODE=true
```

## Entry Points

- **Backend**: `backend/src/main/java/com/studentmgmt/StudentManagementApplication.java` (port 8082)
- **Frontend**: `frontend/src/main.tsx` - React app with MSW demo mode
- **Docker**: `docker-compose.yml` - MySQL (3308) + Backend (8082) + Frontend (3002)

## API Endpoints

| Method | Path | Access | Description |
|--------|------|--------|-------------|
| POST | /api/v1/auth/login | Public | Login + JWT |
| POST | /api/v1/auth/register | Public | Register |
| GET | /api/v1/students | Admin | List students |
| POST | /api/v1/students | Admin | Create student |
| GET | /api/v1/students/{id} | Auth | Get student |
| PUT | /api/v1/students/{id} | Admin | Update student |
| DELETE | /api/v1/students/{id} | Admin | Delete student |
| GET | /api/v1/courses | Auth | List courses |
| POST | /api/v1/courses | Admin | Create course |
| PUT | /api/v1/courses/{id} | Admin | Update course |
| DELETE | /api/v1/courses/{id} | Admin | Delete course |
| GET | /api/v1/enrollments | Auth | List enrollments |
| POST | /api/v1/enrollments | Admin/Faculty | Enroll student |
| DELETE | /api/v1/enrollments/{id} | Admin | Drop enrollment |
| GET | /api/v1/grades/student/{id} | Auth | Student grades |
| POST | /api/v1/grades | Faculty | Enter grade |
| PUT | /api/v1/grades/{id} | Faculty | Update grade |
| GET | /api/v1/grades/student/{id}/gpa | Auth | Calculate GPA |
| GET | /api/v1/transcripts/{studentId}/pdf | Auth | Download PDF |

## Key Dependencies

### Backend
| Dependency | Version | Purpose |
|-----------|---------|---------|
| spring-boot-starter-web | 3.2.5 | REST API |
| spring-boot-starter-data-jpa | 3.2.5 | ORM |
| spring-boot-starter-security | 3.2.5 | Role-based auth |
| springdoc-openapi-starter-webmvc-ui | 2.3.0 | Swagger |
| flyway-core + flyway-mysql | auto | Migrations |
| jjwt-api/impl/jackson | 0.12.5 | JWT |
| com.github.librepdf:openpdf | 1.3.30 | PDF transcript |

### Frontend
| Package | Version | Purpose |
|---------|---------|---------|
| react | 18.3.1 | UI framework |
| @tanstack/react-query | 5.62.2 | Server state |
| zustand | 5.0.2 | Client state + role |
| three | 0.170.0 | 3D graphics |
| @react-three/fiber + drei | 8.x/9.x | React Three.js |
| tailwindcss | 3.4.16 | CSS |
| msw | 2.6.8 | Mock API |

## Business Logic

- **GPA Calculation**: 4.0 scale (A=4.0, B+=3.5, B=3.0, etc.)
- **Role-based access**: ADMIN (full), FACULTY (courses + grades), STUDENT (view only)
- **PDF Transcripts**: Generated with OpenPDF, includes GPA, all courses/grades
- **Demo accounts**: admin/admin123, faculty1/faculty123, student1/student123

## Build Commands

```bash
# Backend
cd backend && JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn clean package

# Frontend
cd frontend && npm ci && npm run build

# Docker
docker-compose up -d
```
