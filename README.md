# Student Management System

A role-based academic management system that allows administrators to manage student records and courses, faculty to enter and manage grades, and students to view their academic progress with interactive 3D visualizations and download PDF transcripts. Modernized from Java Servlets/JSP to a Spring Boot REST API with a React/TypeScript frontend.

**[Live Demo](https://naveenkarasu.github.io/Student-Management-System/)** (runs with mock data, no backend required)

## Tech Stack

### Backend
- **Java 17**, Spring Boot 3.2.5, Spring Data JPA, Spring Security 6
- **JWT Authentication** with role-based access control (Admin/Faculty/Student)
- **REST API** with versioned endpoints (`/api/v1/`)
- **Swagger/OpenAPI** documentation (`/swagger-ui.html`)
- **PDF Transcripts:** OpenPDF 1.3.35 (LGPL fork of iText 5)
- **Database:** MySQL 8.0 with Flyway migrations
- **Testing:** JUnit 5, Mockito
- **Build Tool:** Apache Maven

### Frontend
- **React 18** with TypeScript 5
- **Vite 5** for fast builds and HMR
- **Tailwind CSS** + shadcn/ui components
- **Three.js** (React Three Fiber + Drei):
  - **3D Grade Chart** - Cylinders per course, height = marks, color = grade
  - **Academic Progress Orbit** - Courses as orbiting planets, size = grade points
- **TanStack Query v5** for server state management
- **Zustand** for auth state
- **MSW** (Mock Service Worker) for demo mode
- **Axios** with JWT interceptors

## Quick Start with Docker

```bash
docker-compose up --build
```

This starts MySQL (port 3308), the Spring Boot backend (port 8082), and the React frontend (port 3002).

Open: `http://localhost:3002`

## Manual Setup

### Prerequisites

- JDK 17
- Node.js 20+
- Apache Maven 3.x
- MySQL Server 8.0+

### Backend

```bash
cd backend

# Database is auto-created by Flyway migrations
# Update credentials in src/main/resources/application.properties if needed

mvn spring-boot:run
```

Backend runs at `http://localhost:8082`
Swagger UI at `http://localhost:8082/swagger-ui.html`

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend runs at `http://localhost:5173`

## Default Credentials

| Role    | Username   | Password     |
|---------|------------|--------------|
| Admin   | admin      | admin123     |
| Faculty | faculty1   | faculty123   |
| Student | student1   | student123   |

## Features

- **Role-Based Access Control** - Different dashboards and permissions for admin, faculty, and students
- **3D Grade Visualization** - Interactive Three.js bar chart showing grades across courses
- **Academic Progress Orbit** - 3D planetary visualization of course performance
- **Student Management** - Full CRUD for student records with department/semester tracking
- **Course Management** - Create courses with codes, credits, and faculty assignments
- **Enrollment** - Enroll students in courses per semester
- **Grade Management** - Faculty can enter marks; letter grades and GPA auto-calculated
- **PDF Transcript** - Download formatted semester-wise transcripts with GPA
- **JWT Authentication** - Secure stateless API with BCrypt password hashing
- **Demo Mode** - Full frontend experience with MSW mock data (for GitHub Pages)

## GPA Calculation

| Grade | Grade Points |
|-------|-------------|
| A+    | 4.0         |
| A     | 4.0         |
| A-    | 3.7         |
| B+    | 3.3         |
| B     | 3.0         |
| B-    | 2.7         |
| C+    | 2.3         |
| C     | 2.0         |
| C-    | 1.7         |
| D+    | 1.3         |
| D     | 1.0         |
| F     | 0.0         |

GPA is credit-weighted: `GPA = Σ(credits × gradePoints) / Σ(credits)`

## REST API Endpoints

| Method | URL                                  | Description              | Access       |
|--------|--------------------------------------|--------------------------|--------------|
| POST   | /api/v1/auth/login                   | Authenticate user        | Public       |
| GET    | /api/v1/students                     | List all students        | Admin        |
| POST   | /api/v1/students                     | Add a new student        | Admin        |
| GET    | /api/v1/students/{id}                | Get student details      | Admin, Self  |
| PUT    | /api/v1/students/{id}                | Update a student         | Admin        |
| DELETE | /api/v1/students/{id}                | Delete a student         | Admin        |
| GET    | /api/v1/courses                      | List all courses         | All          |
| POST   | /api/v1/courses                      | Create a course          | Admin        |
| GET    | /api/v1/courses/{id}                 | Get course details       | All          |
| PUT    | /api/v1/courses/{id}                 | Update a course          | Admin        |
| DELETE | /api/v1/courses/{id}                 | Delete a course          | Admin        |
| GET    | /api/v1/enrollments                  | List enrollments         | Admin, Faculty|
| POST   | /api/v1/enrollments                  | Enroll student in course | Admin        |
| GET    | /api/v1/grades/student/{id}          | Get student grades       | Admin, Self  |
| POST   | /api/v1/grades                       | Assign grade             | Faculty      |
| PUT    | /api/v1/grades/{id}                  | Update grade             | Faculty      |
| GET    | /api/v1/transcripts/{studentId}/pdf  | Download PDF transcript  | Admin, Self  |

Full API docs available at `/swagger-ui.html` when the backend is running.

## Project Structure

```
student-management-system/
├── docker-compose.yml
├── backend/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/
│       ├── java/com/studentmgmt/
│       │   ├── config/          # Security, JWT, OpenAPI config
│       │   ├── controller/      # REST API controllers
│       │   ├── model/           # JPA entities + enums
│       │   ├── repository/      # Spring Data repositories
│       │   └── service/         # Business logic (GPA, transcripts)
│       └── resources/
│           ├── application.properties
│           └── db/migration/    # Flyway SQL migrations
├── frontend/
│   ├── package.json
│   ├── vite.config.ts
│   ├── Dockerfile               # Nginx-based production container
│   └── src/
│       ├── components/          # React components + Three.js visualizations
│       ├── pages/               # Role-based route pages
│       ├── hooks/               # Custom hooks
│       ├── api/                 # API client layer
│       ├── mocks/               # MSW handlers + demo data
│       └── types/               # TypeScript types
└── legacy/                      # Original Servlets/JSP code (branch)
```

## License

This project was made for educational purposes. Feel free to use or modify it.
