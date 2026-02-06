package com.studentmgmt.controller;

import com.studentmgmt.model.Grade;
import com.studentmgmt.model.Student;
import com.studentmgmt.service.GradeService;
import com.studentmgmt.service.StudentService;
import com.studentmgmt.service.TranscriptService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transcripts")
public class TranscriptController {

    private final TranscriptService transcriptService;
    private final StudentService studentService;
    private final GradeService gradeService;

    public TranscriptController(TranscriptService transcriptService,
                                StudentService studentService,
                                GradeService gradeService) {
        this.transcriptService = transcriptService;
        this.studentService = studentService;
        this.gradeService = gradeService;
    }

    @GetMapping("/{studentId}/pdf")
    public void generateTranscript(@PathVariable Long studentId, HttpServletResponse response) throws IOException {
        Student student = studentService.getStudentById(studentId);
        List<Grade> grades = gradeService.getGradesByStudent(studentId);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=transcript_" + student.getRollNumber() + ".pdf");

        transcriptService.generateTranscript(student, grades, response.getOutputStream());
        response.getOutputStream().flush();
    }
}
