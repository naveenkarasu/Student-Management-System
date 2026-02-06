package com.studentmgmt.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.studentmgmt.model.Grade;
import com.studentmgmt.model.Student;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TranscriptService {

    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 18, Font.BOLD, Color.DARK_GRAY);
    private static final Font SUBTITLE_FONT = new Font(Font.HELVETICA, 12, Font.BOLD, Color.DARK_GRAY);
    private static final Font HEADER_FONT = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
    private static final Font BODY_FONT = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK);
    private static final Font BOLD_FONT = new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK);
    private static final Font SMALL_FONT = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.GRAY);

    private static final Color HEADER_BG = new Color(44, 62, 80);
    private static final Color ROW_ALT_BG = new Color(245, 245, 245);

    public void generateTranscript(Student student, List<Grade> grades, OutputStream out) {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            addHeader(document, student);
            addStudentInfo(document, student);

            Map<String, List<Grade>> gradesBySemester = grades.stream()
                    .collect(Collectors.groupingBy(
                            g -> g.getEnrollment().getSemester(),
                            LinkedHashMap::new,
                            Collectors.toList()
                    ));

            double totalWeightedPoints = 0.0;
            int totalCredits = 0;

            for (Map.Entry<String, List<Grade>> entry : gradesBySemester.entrySet()) {
                String semester = entry.getKey();
                List<Grade> semesterGrades = entry.getValue();

                addSemesterSection(document, semester, semesterGrades);

                double semesterWeightedPoints = 0.0;
                int semesterCredits = 0;
                for (Grade g : semesterGrades) {
                    int credits = g.getEnrollment().getCourse().getCredits();
                    semesterWeightedPoints += credits * g.getGradePoints();
                    semesterCredits += credits;
                }

                totalWeightedPoints += semesterWeightedPoints;
                totalCredits += semesterCredits;

                double semesterGPA = semesterCredits > 0
                        ? Math.round((semesterWeightedPoints / semesterCredits) * 100.0) / 100.0
                        : 0.0;

                addSemesterSummary(document, semesterCredits, semesterGPA);
            }

            double cumulativeGPA = totalCredits > 0
                    ? Math.round((totalWeightedPoints / totalCredits) * 100.0) / 100.0
                    : 0.0;

            addCumulativeSummary(document, totalCredits, cumulativeGPA);
            addFooter(document);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Failed to generate transcript PDF", e);
        }
    }

    private void addHeader(Document document, Student student) throws DocumentException {
        Paragraph title = new Paragraph("OFFICIAL ACADEMIC TRANSCRIPT", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph institution = new Paragraph("Student Management System", SUBTITLE_FONT);
        institution.setAlignment(Element.ALIGN_CENTER);
        institution.setSpacingAfter(20);
        document.add(institution);

        document.add(new Paragraph(" "));
    }

    private void addStudentInfo(Document document, Student student) throws DocumentException {
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingAfter(20);

        addInfoRow(infoTable, "Student Name:", student.getName());
        addInfoRow(infoTable, "Roll Number:", student.getRollNumber() != null ? student.getRollNumber() : "N/A");
        addInfoRow(infoTable, "Department:", student.getDepartment() != null ? student.getDepartment() : "N/A");
        addInfoRow(infoTable, "Current Semester:", String.valueOf(student.getSemester()));
        addInfoRow(infoTable, "Date Issued:", LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));

        document.add(infoTable);
    }

    private void addInfoRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, BOLD_FONT));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(4);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, BODY_FONT));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(4);
        table.addCell(valueCell);
    }

    private void addSemesterSection(Document document, String semester, List<Grade> grades) throws DocumentException {
        Paragraph semesterTitle = new Paragraph(semester, SUBTITLE_FONT);
        semesterTitle.setSpacingBefore(15);
        semesterTitle.setSpacingAfter(5);
        document.add(semesterTitle);

        PdfPTable table = new PdfPTable(new float[]{15, 35, 10, 10, 10, 20});
        table.setWidthPercentage(100);
        table.setSpacingAfter(10);

        addTableHeader(table, "Code");
        addTableHeader(table, "Course Name");
        addTableHeader(table, "Credits");
        addTableHeader(table, "Marks");
        addTableHeader(table, "Grade");
        addTableHeader(table, "Grade Points");

        boolean alternate = false;
        for (Grade grade : grades) {
            Color bgColor = alternate ? ROW_ALT_BG : Color.WHITE;

            addTableCell(table, grade.getEnrollment().getCourse().getCourseCode(), bgColor);
            addTableCell(table, grade.getEnrollment().getCourse().getCourseName(), bgColor);
            addTableCell(table, String.valueOf(grade.getEnrollment().getCourse().getCredits()), bgColor);
            addTableCell(table, String.format("%.1f", grade.getMarks()), bgColor);
            addTableCell(table, grade.getGrade(), bgColor);
            addTableCell(table, String.format("%.1f", grade.getGradePoints()), bgColor);

            alternate = !alternate;
        }

        document.add(table);
    }

    private void addTableHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, HEADER_FONT));
        cell.setBackgroundColor(HEADER_BG);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(6);
        table.addCell(cell);
    }

    private void addTableCell(PdfPTable table, String text, Color bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, BODY_FONT));
        cell.setBackgroundColor(bgColor);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addSemesterSummary(Document document, int credits, double gpa) throws DocumentException {
        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(50);
        summaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        summaryTable.setSpacingAfter(10);

        PdfPCell creditsLabel = new PdfPCell(new Phrase("Semester Credits:", BOLD_FONT));
        creditsLabel.setBorder(Rectangle.TOP);
        creditsLabel.setPadding(4);
        summaryTable.addCell(creditsLabel);

        PdfPCell creditsValue = new PdfPCell(new Phrase(String.valueOf(credits), BODY_FONT));
        creditsValue.setBorder(Rectangle.TOP);
        creditsValue.setPadding(4);
        creditsValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        summaryTable.addCell(creditsValue);

        PdfPCell gpaLabel = new PdfPCell(new Phrase("Semester GPA:", BOLD_FONT));
        gpaLabel.setBorder(Rectangle.NO_BORDER);
        gpaLabel.setPadding(4);
        summaryTable.addCell(gpaLabel);

        PdfPCell gpaValue = new PdfPCell(new Phrase(String.format("%.2f", gpa), BODY_FONT));
        gpaValue.setBorder(Rectangle.NO_BORDER);
        gpaValue.setPadding(4);
        gpaValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        summaryTable.addCell(gpaValue);

        document.add(summaryTable);
    }

    private void addCumulativeSummary(Document document, int totalCredits, double cumulativeGPA) throws DocumentException {
        document.add(new Paragraph(" "));

        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(60);
        summaryTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        summaryTable.setSpacingBefore(10);

        PdfPCell headerCell = new PdfPCell(new Phrase("CUMULATIVE SUMMARY", SUBTITLE_FONT));
        headerCell.setColspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setPadding(8);
        headerCell.setBackgroundColor(new Color(236, 240, 241));
        summaryTable.addCell(headerCell);

        PdfPCell totalCreditsLabel = new PdfPCell(new Phrase("Total Credits Earned:", BOLD_FONT));
        totalCreditsLabel.setPadding(6);
        summaryTable.addCell(totalCreditsLabel);

        PdfPCell totalCreditsValue = new PdfPCell(new Phrase(String.valueOf(totalCredits), BODY_FONT));
        totalCreditsValue.setPadding(6);
        totalCreditsValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        summaryTable.addCell(totalCreditsValue);

        PdfPCell cumulativeGPALabel = new PdfPCell(new Phrase("Cumulative GPA:", BOLD_FONT));
        cumulativeGPALabel.setPadding(6);
        summaryTable.addCell(cumulativeGPALabel);

        PdfPCell cumulativeGPAValue = new PdfPCell(new Phrase(String.format("%.2f / 4.00", cumulativeGPA), BOLD_FONT));
        cumulativeGPAValue.setPadding(6);
        cumulativeGPAValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        summaryTable.addCell(cumulativeGPAValue);

        document.add(summaryTable);
    }

    private void addFooter(Document document) throws DocumentException {
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        Paragraph footer = new Paragraph(
                "This is a system-generated transcript. Generated on " +
                        LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")) + ".",
                SMALL_FONT
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        Paragraph disclaimer = new Paragraph(
                "This document is not valid without the official seal of the institution.",
                SMALL_FONT
        );
        disclaimer.setAlignment(Element.ALIGN_CENTER);
        document.add(disclaimer);
    }
}
