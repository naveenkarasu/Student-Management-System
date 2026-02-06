package com.studentmgmt.service;

import com.studentmgmt.dto.GradeRequest;
import com.studentmgmt.model.*;
import com.studentmgmt.repository.EnrollmentRepository;
import com.studentmgmt.repository.GradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GradeServiceTest {

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private GradeService gradeService;

    private Student student;
    private Course course1;
    private Course course2;
    private Enrollment enrollment1;
    private Enrollment enrollment2;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setStudentId(1L);
        student.setName("John Doe");
        student.setRollNumber("CS2024001");

        course1 = new Course();
        course1.setCourseId(1L);
        course1.setCourseCode("CS101");
        course1.setCourseName("Introduction to Programming");
        course1.setCredits(4);

        course2 = new Course();
        course2.setCourseId(2L);
        course2.setCourseCode("MT101");
        course2.setCourseName("Calculus I");
        course2.setCredits(3);

        enrollment1 = new Enrollment();
        enrollment1.setEnrollmentId(1L);
        enrollment1.setStudent(student);
        enrollment1.setCourse(course1);
        enrollment1.setSemester("Fall 2024");

        enrollment2 = new Enrollment();
        enrollment2.setEnrollmentId(2L);
        enrollment2.setStudent(student);
        enrollment2.setCourse(course2);
        enrollment2.setSemester("Fall 2024");
    }

    @Test
    void testCalculateLetterGrade_APlus() {
        assertEquals("A+", gradeService.calculateLetterGrade(95));
        assertEquals("A+", gradeService.calculateLetterGrade(90));
    }

    @Test
    void testCalculateLetterGrade_A() {
        assertEquals("A", gradeService.calculateLetterGrade(89));
        assertEquals("A", gradeService.calculateLetterGrade(85));
    }

    @Test
    void testCalculateLetterGrade_AMinus() {
        assertEquals("A-", gradeService.calculateLetterGrade(84));
        assertEquals("A-", gradeService.calculateLetterGrade(80));
    }

    @Test
    void testCalculateLetterGrade_BPlus() {
        assertEquals("B+", gradeService.calculateLetterGrade(79));
        assertEquals("B+", gradeService.calculateLetterGrade(75));
    }

    @Test
    void testCalculateLetterGrade_B() {
        assertEquals("B", gradeService.calculateLetterGrade(74));
        assertEquals("B", gradeService.calculateLetterGrade(70));
    }

    @Test
    void testCalculateLetterGrade_BMinus() {
        assertEquals("B-", gradeService.calculateLetterGrade(69));
        assertEquals("B-", gradeService.calculateLetterGrade(65));
    }

    @Test
    void testCalculateLetterGrade_CPlus() {
        assertEquals("C+", gradeService.calculateLetterGrade(64));
        assertEquals("C+", gradeService.calculateLetterGrade(60));
    }

    @Test
    void testCalculateLetterGrade_C() {
        assertEquals("C", gradeService.calculateLetterGrade(59));
        assertEquals("C", gradeService.calculateLetterGrade(55));
    }

    @Test
    void testCalculateLetterGrade_CMinus() {
        assertEquals("C-", gradeService.calculateLetterGrade(54));
        assertEquals("C-", gradeService.calculateLetterGrade(50));
    }

    @Test
    void testCalculateLetterGrade_DPlus() {
        assertEquals("D+", gradeService.calculateLetterGrade(49));
        assertEquals("D+", gradeService.calculateLetterGrade(45));
    }

    @Test
    void testCalculateLetterGrade_D() {
        assertEquals("D", gradeService.calculateLetterGrade(44));
        assertEquals("D", gradeService.calculateLetterGrade(40));
    }

    @Test
    void testCalculateLetterGrade_F() {
        assertEquals("F", gradeService.calculateLetterGrade(39));
        assertEquals("F", gradeService.calculateLetterGrade(0));
    }

    @Test
    void testGradePoints() {
        Grade grade = new Grade();

        grade.setGrade("A+");
        assertEquals(4.0, grade.getGradePoints());

        grade.setGrade("A");
        assertEquals(4.0, grade.getGradePoints());

        grade.setGrade("A-");
        assertEquals(3.7, grade.getGradePoints());

        grade.setGrade("B+");
        assertEquals(3.3, grade.getGradePoints());

        grade.setGrade("B");
        assertEquals(3.0, grade.getGradePoints());

        grade.setGrade("B-");
        assertEquals(2.7, grade.getGradePoints());

        grade.setGrade("C+");
        assertEquals(2.3, grade.getGradePoints());

        grade.setGrade("C");
        assertEquals(2.0, grade.getGradePoints());

        grade.setGrade("C-");
        assertEquals(1.7, grade.getGradePoints());

        grade.setGrade("D+");
        assertEquals(1.3, grade.getGradePoints());

        grade.setGrade("D");
        assertEquals(1.0, grade.getGradePoints());

        grade.setGrade("F");
        assertEquals(0.0, grade.getGradePoints());
    }

    @Test
    void testGradePointsWithNullGrade() {
        Grade grade = new Grade();
        grade.setGrade(null);
        assertEquals(0.0, grade.getGradePoints());
    }

    @Test
    void testCalculateGPA_WeightedByCredits() {
        // CS101: 4 credits, grade A+ (4.0 grade points)
        Grade grade1 = new Grade();
        grade1.setEnrollment(enrollment1);
        grade1.setMarks(92);
        grade1.setGrade("A+");

        // MT101: 3 credits, grade B+ (3.3 grade points)
        Grade grade2 = new Grade();
        grade2.setEnrollment(enrollment2);
        grade2.setMarks(78);
        grade2.setGrade("B+");

        when(gradeRepository.findByStudentId(1L)).thenReturn(Arrays.asList(grade1, grade2));

        double gpa = gradeService.calculateGPA(1L);

        // GPA = (4*4.0 + 3*3.3) / (4+3) = (16.0 + 9.9) / 7 = 25.9 / 7 = 3.70
        assertEquals(3.7, gpa, 0.01);
    }

    @Test
    void testCalculateGPA_NoGrades() {
        when(gradeRepository.findByStudentId(1L)).thenReturn(List.of());

        double gpa = gradeService.calculateGPA(1L);
        assertEquals(0.0, gpa);
    }

    @Test
    void testCalculateSemesterGPA() {
        Grade grade1 = new Grade();
        grade1.setEnrollment(enrollment1);
        grade1.setMarks(92);
        grade1.setGrade("A+");

        Grade grade2 = new Grade();
        grade2.setEnrollment(enrollment2);
        grade2.setMarks(78);
        grade2.setGrade("B+");

        when(gradeRepository.findByStudentIdAndSemester(1L, "Fall 2024"))
                .thenReturn(Arrays.asList(grade1, grade2));

        double gpa = gradeService.calculateSemesterGPA(1L, "Fall 2024");

        // GPA = (4*4.0 + 3*3.3) / (4+3) = 25.9 / 7 = 3.70
        assertEquals(3.7, gpa, 0.01);
    }

    @Test
    void testAssignGrade_WithLetterGrade() {
        GradeRequest request = new GradeRequest(1L, 92.0, "A+");

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment1));
        when(gradeRepository.findByEnrollment(enrollment1)).thenReturn(Optional.empty());
        when(gradeRepository.save(any(Grade.class))).thenAnswer(invocation -> {
            Grade saved = invocation.getArgument(0);
            saved.setGradeId(1L);
            return saved;
        });

        Grade result = gradeService.assignGrade(request);

        assertNotNull(result);
        assertEquals(92.0, result.getMarks());
        assertEquals("A+", result.getGrade());
        verify(gradeRepository).save(any(Grade.class));
    }

    @Test
    void testAssignGrade_AutoCalculateLetterGrade() {
        GradeRequest request = new GradeRequest(1L, 78.0, null);

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment1));
        when(gradeRepository.findByEnrollment(enrollment1)).thenReturn(Optional.empty());
        when(gradeRepository.save(any(Grade.class))).thenAnswer(invocation -> {
            Grade saved = invocation.getArgument(0);
            saved.setGradeId(1L);
            return saved;
        });

        Grade result = gradeService.assignGrade(request);

        assertNotNull(result);
        assertEquals(78.0, result.getMarks());
        assertEquals("B+", result.getGrade());
    }

    @Test
    void testAssignGrade_UpdateExistingGrade() {
        Grade existingGrade = new Grade();
        existingGrade.setGradeId(1L);
        existingGrade.setEnrollment(enrollment1);
        existingGrade.setMarks(60);
        existingGrade.setGrade("C+");

        GradeRequest request = new GradeRequest(1L, 85.0, "A");

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment1));
        when(gradeRepository.findByEnrollment(enrollment1)).thenReturn(Optional.of(existingGrade));
        when(gradeRepository.save(any(Grade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Grade result = gradeService.assignGrade(request);

        assertEquals(1L, result.getGradeId());
        assertEquals(85.0, result.getMarks());
        assertEquals("A", result.getGrade());
    }

    @Test
    void testAssignGrade_EnrollmentNotFound() {
        GradeRequest request = new GradeRequest(999L, 85.0, "A");

        when(enrollmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> gradeService.assignGrade(request));
    }

    @Test
    void testUpdateGrade() {
        Grade existingGrade = new Grade();
        existingGrade.setGradeId(1L);
        existingGrade.setEnrollment(enrollment1);
        existingGrade.setMarks(60);
        existingGrade.setGrade("C+");

        GradeRequest request = new GradeRequest(1L, 90.0, "A+");

        when(gradeRepository.findById(1L)).thenReturn(Optional.of(existingGrade));
        when(gradeRepository.save(any(Grade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Grade result = gradeService.updateGrade(1L, request);

        assertEquals(90.0, result.getMarks());
        assertEquals("A+", result.getGrade());
    }

    @Test
    void testUpdateGrade_NotFound() {
        GradeRequest request = new GradeRequest(1L, 90.0, "A+");

        when(gradeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> gradeService.updateGrade(999L, request));
    }

    @Test
    void testToResponse() {
        Grade grade = new Grade();
        grade.setGradeId(1L);
        grade.setEnrollment(enrollment1);
        grade.setMarks(92.0);
        grade.setGrade("A+");

        var response = gradeService.toResponse(grade);

        assertEquals(1L, response.getGradeId());
        assertEquals(1L, response.getEnrollmentId());
        assertEquals("CS101", response.getCourseCode());
        assertEquals("Introduction to Programming", response.getCourseName());
        assertEquals(4, response.getCredits());
        assertEquals(92.0, response.getMarks());
        assertEquals("A+", response.getLetterGrade());
        assertEquals(4.0, response.getGradePoints());
        assertEquals("Fall 2024", response.getSemester());
    }

    @Test
    void testCalculateGPA_AllSameGrade() {
        // Both courses with A (4.0) - GPA should be 4.0 regardless of credits
        Grade grade1 = new Grade();
        grade1.setEnrollment(enrollment1);
        grade1.setGrade("A");

        Grade grade2 = new Grade();
        grade2.setEnrollment(enrollment2);
        grade2.setGrade("A");

        when(gradeRepository.findByStudentId(1L)).thenReturn(Arrays.asList(grade1, grade2));

        double gpa = gradeService.calculateGPA(1L);
        assertEquals(4.0, gpa, 0.01);
    }

    @Test
    void testCalculateGPA_AllFailing() {
        Grade grade1 = new Grade();
        grade1.setEnrollment(enrollment1);
        grade1.setGrade("F");

        Grade grade2 = new Grade();
        grade2.setEnrollment(enrollment2);
        grade2.setGrade("F");

        when(gradeRepository.findByStudentId(1L)).thenReturn(Arrays.asList(grade1, grade2));

        double gpa = gradeService.calculateGPA(1L);
        assertEquals(0.0, gpa, 0.01);
    }
}
