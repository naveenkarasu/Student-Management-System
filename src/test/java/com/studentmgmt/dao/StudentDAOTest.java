package com.studentmgmt.dao;

import com.studentmgmt.model.Student;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

import java.util.List;

import static org.junit.Assert.*;

/**
 * JUnit test class for StudentDAO.
 * Note: These tests require a running MySQL database with the schema.sql applied.
 * Tests are marked with @Ignore by default to prevent CI failures.
 */
public class StudentDAOTest {

    private StudentDAO studentDAO;

    @Before
    public void setUp() {
        studentDAO = new StudentDAO();
    }

    @Test
    @Ignore("Requires database connection")
    public void testGetAllStudents() {
        List<Student> students = studentDAO.getAllStudents();
        assertNotNull("Students list should not be null", students);
        // Should have at least the sample students from schema.sql
        assertTrue("Should have at least 2 students", students.size() >= 2);
    }

    @Test
    @Ignore("Requires database connection")
    public void testGetStudentById() {
        // Assuming student with ID 1 exists (from sample data)
        Student student = studentDAO.getStudentById(1);
        assertNotNull("Student should not be null", student);
        assertEquals("Student ID should be 1", 1, student.getStudentId());
    }

    @Test
    @Ignore("Requires database connection")
    public void testGetStudentByRollNumber() {
        // Assuming student with roll number CS2018001 exists
        Student student = studentDAO.getStudentByRollNumber("CS2018001");
        assertNotNull("Student should not be null", student);
        assertEquals("Roll number should match", "CS2018001", student.getRollNumber());
    }

    @Test
    @Ignore("Requires database connection")
    public void testCreateAndDeleteStudent() {
        // Create a test student
        Student student = new Student();
        student.setRollNumber("TEST001");
        student.setName("Test Student");
        student.setEmail("test@test.com");
        student.setDepartment("Computer Science");
        student.setSemester(1);

        // Create student
        int studentId = studentDAO.createStudent(student);
        assertTrue("Student ID should be positive", studentId > 0);

        // Verify student was created
        Student created = studentDAO.getStudentById(studentId);
        assertNotNull("Created student should exist", created);
        assertEquals("Name should match", "Test Student", created.getName());

        // Delete student
        boolean deleted = studentDAO.deleteStudent(studentId);
        assertTrue("Delete should return true", deleted);

        // Verify student was deleted
        Student deletedStudent = studentDAO.getStudentById(studentId);
        assertNull("Deleted student should not exist", deletedStudent);
    }

    @Test
    @Ignore("Requires database connection")
    public void testUpdateStudent() {
        // Get existing student
        Student student = studentDAO.getStudentById(1);
        assertNotNull("Student should exist", student);

        // Store original values
        String originalName = student.getName();

        // Update student
        student.setName("Updated Name");
        boolean updated = studentDAO.updateStudent(student);
        assertTrue("Update should return true", updated);

        // Verify update
        Student updatedStudent = studentDAO.getStudentById(1);
        assertEquals("Name should be updated", "Updated Name", updatedStudent.getName());

        // Restore original value
        student.setName(originalName);
        studentDAO.updateStudent(student);
    }

    @Test
    @Ignore("Requires database connection")
    public void testRollNumberExists() {
        // Test with existing roll number
        boolean exists = studentDAO.rollNumberExists("CS2018001");
        assertTrue("Roll number should exist", exists);

        // Test with non-existing roll number
        boolean notExists = studentDAO.rollNumberExists("NONEXISTENT123");
        assertFalse("Roll number should not exist", notExists);
    }

    @Test
    @Ignore("Requires database connection")
    public void testSearchStudents() {
        // Search by name
        List<Student> results = studentDAO.searchStudents("Alice");
        assertNotNull("Search results should not be null", results);
        assertTrue("Should find at least one student", results.size() >= 1);

        // Search by roll number
        List<Student> rollResults = studentDAO.searchStudents("CS2018");
        assertNotNull("Search results should not be null", rollResults);
        assertTrue("Should find students with CS2018 roll number", rollResults.size() >= 1);
    }

    @Test
    @Ignore("Requires database connection")
    public void testGetStudentsByDepartment() {
        List<Student> students = studentDAO.getStudentsByDepartment("Computer Science");
        assertNotNull("Students list should not be null", students);

        for (Student student : students) {
            assertEquals("Department should be Computer Science",
                        "Computer Science", student.getDepartment());
        }
    }

    @Test
    @Ignore("Requires database connection")
    public void testGetStudentCount() {
        int count = studentDAO.getStudentCount();
        assertTrue("Count should be non-negative", count >= 0);
    }

    @Test
    public void testStudentModel() {
        // Test Student model without database
        Student student = new Student();
        student.setStudentId(1);
        student.setUserId(10);
        student.setRollNumber("TEST123");
        student.setName("Test Name");
        student.setEmail("test@example.com");
        student.setDepartment("CS");
        student.setSemester(5);

        assertEquals("Student ID should match", 1, student.getStudentId());
        assertEquals("User ID should match", 10, student.getUserId());
        assertEquals("Roll number should match", "TEST123", student.getRollNumber());
        assertEquals("Name should match", "Test Name", student.getName());
        assertEquals("Email should match", "test@example.com", student.getEmail());
        assertEquals("Department should match", "CS", student.getDepartment());
        assertEquals("Semester should match", 5, student.getSemester());
    }
}
