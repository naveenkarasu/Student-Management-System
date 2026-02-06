# Student Management System

A role-based academic management system that allows administrators to manage student records and courses, faculty to enter and manage grades, and students to view their academic progress and download PDF transcripts. Features session-based authentication with three access levels.

## Tech Stack

- **Backend:** Java 8, Servlets, JSP, JDBC
- **Frontend:** HTML, CSS, Bootstrap 3, JavaScript
- **Database:** MySQL 5.7
- **PDF Generation:** iText 5.x
- **Build Tool:** Apache Maven
- **Server:** Apache Tomcat 8

## Prerequisites

- JDK 1.8 (Java 8)
- Apache Maven 3.x
- MySQL Server 5.7+
- Apache Tomcat 8.x

## Database Setup

1. Open MySQL and create the database:

```sql
CREATE DATABASE student_management_db;
USE student_management_db;
```

2. Run the schema file:

```bash
mysql -u root -p student_management_db < sql/schema.sql
```

3. Load sample data:

```bash
mysql -u root -p student_management_db < sql/seed-data.sql
```

4. Update database credentials in `src/main/resources/db.properties` if your MySQL username/password is different from default (`root` / `root`).

## Default Login Credentials

These are for testing purposes only:

| Role    | Username   | Password     |
|---------|------------|--------------|
| Admin   | admin      | admin123     |
| Faculty | faculty1   | faculty123   |
| Student | student1   | student123   |

## Build and Deployment

1. Build the project:

```bash
cd student-management-system
mvn clean package
```

2. Copy the WAR file to Tomcat:

```bash
cp target/student-management-system.war /path/to/tomcat/webapps/
```

3. Start Tomcat and open in browser:

```
http://localhost:8080/student-management-system/
```

## Features

- **Role-Based Access Control** - Different dashboards and permissions for admin, faculty, and students
- **Student Management** - Add, update, delete, and search student records
- **Course Management** - Create courses with codes, credits, and faculty assignments
- **Enrollment** - Enroll students in courses per semester
- **Grade Management** - Faculty can enter marks, grades are auto-calculated
- **PDF Transcript Generation** - Download semester-wise transcripts as PDF using iText
- **Search and Filter** - Search students by roll number, name, or department

## Project Structure

```
student-management-system/
├── pom.xml
├── sql/
│   ├── schema.sql
│   └── seed-data.sql
├── src/
│   ├── main/
│   │   ├── java/com/studentmgmt/
│   │   │   ├── dao/           # Data Access Objects
│   │   │   ├── model/         # Java Beans
│   │   │   ├── servlet/       # Servlet Controllers
│   │   │   ├── filter/        # Auth Filter
│   │   │   └── util/          # DB Connection, PDF Generator
│   │   ├── resources/
│   │   │   └── db.properties
│   │   └── webapp/
│   │       ├── WEB-INF/web.xml
│   │       ├── css/style.css
│   │       ├── login.jsp
│   │       ├── dashboard.jsp
│   │       ├── admin/         # Admin pages
│   │       ├── faculty/       # Faculty pages
│   │       └── student/       # Student pages
│   └── test/
│       └── java/com/studentmgmt/dao/
└── README.md
```

## Known Issues

- Password hashing is not implemented yet (passwords stored in plain text)
- No email verification for new accounts
- Pagination not added for large student lists

## License

This project was made for educational purposes. Feel free to use or modify it.
