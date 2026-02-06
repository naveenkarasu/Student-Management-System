package com.studentmgmt.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.studentmgmt.model.Grade;
import com.studentmgmt.model.Student;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * PDF Generator utility for creating student transcripts.
 * Uses iText 5.x library for PDF generation.
 */
public class PDFGenerator {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);

    private static final BaseColor HEADER_BG_COLOR = new BaseColor(52, 73, 94);
    private static final BaseColor LIGHT_GRAY = new BaseColor(236, 240, 241);

    /**
     * Generate a transcript PDF for a student.
     *
     * @param student the student information
     * @param grades list of grades for the student
     * @param outputStream the output stream to write the PDF to
     * @throws Exception if PDF generation fails
     */
    public static void generateTranscript(Student student, List<Grade> grades, OutputStream outputStream)
            throws Exception {

        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);

        document.open();

        // Add header
        addHeader(document, student);

        // Add student information
        addStudentInfo(document, student);

        // Add grades table
        addGradesTable(document, grades);

        // Add GPA summary
        addGPASummary(document, grades);

        // Add footer
        addFooter(document);

        document.close();
    }

    private static void addHeader(Document document, Student student) throws DocumentException {
        // University name
        Paragraph title = new Paragraph("UNIVERSITY TRANSCRIPT", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph subtitle = new Paragraph("Official Academic Record", NORMAL_FONT);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        document.add(subtitle);

        document.add(Chunk.NEWLINE);

        // Add a separator line
        LineSeparator separator = new LineSeparator();
        separator.setLineColor(HEADER_BG_COLOR);
        document.add(new Chunk(separator));

        document.add(Chunk.NEWLINE);
    }

    private static void addStudentInfo(Document document, Student student) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 2, 1, 2});

        // Row 1
        addInfoCell(table, "Student Name:", true);
        addInfoCell(table, student.getName(), false);
        addInfoCell(table, "Roll Number:", true);
        addInfoCell(table, student.getRollNumber(), false);

        // Row 2
        addInfoCell(table, "Department:", true);
        addInfoCell(table, student.getDepartment(), false);
        addInfoCell(table, "Semester:", true);
        addInfoCell(table, String.valueOf(student.getSemester()), false);

        // Row 3
        addInfoCell(table, "Email:", true);
        addInfoCell(table, student.getEmail() != null ? student.getEmail() : "N/A", false);
        addInfoCell(table, "Date:", true);
        addInfoCell(table, new SimpleDateFormat("dd-MM-yyyy").format(new Date()), false);

        document.add(table);
        document.add(Chunk.NEWLINE);
    }

    private static void addInfoCell(PdfPTable table, String text, boolean isLabel) {
        PdfPCell cell = new PdfPCell(new Phrase(text, isLabel ? HEADER_FONT : NORMAL_FONT));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        if (isLabel) {
            cell.setBackgroundColor(LIGHT_GRAY);
        }
        table.addCell(cell);
    }

    private static void addGradesTable(Document document, List<Grade> grades) throws DocumentException {
        Paragraph heading = new Paragraph("Academic Performance", HEADER_FONT);
        heading.setSpacingBefore(20);
        heading.setSpacingAfter(10);
        document.add(heading);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 2, 3, 1, 1, 1});

        // Table headers
        addTableHeader(table, "S.No");
        addTableHeader(table, "Course Code");
        addTableHeader(table, "Course Name");
        addTableHeader(table, "Credits");
        addTableHeader(table, "Marks");
        addTableHeader(table, "Grade");

        // Table rows
        int serialNo = 1;
        for (Grade grade : grades) {
            addTableCell(table, String.valueOf(serialNo++), true);
            addTableCell(table, grade.getCourseCode(), false);
            addTableCell(table, grade.getCourseName(), false);
            addTableCell(table, String.valueOf(grade.getCredits()), true);
            addTableCell(table, String.format("%.2f", grade.getMarks()), true);
            addTableCell(table, grade.getGrade(), true);
        }

        document.add(table);
    }

    private static void addTableHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE)));
        cell.setBackgroundColor(HEADER_BG_COLOR);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(8);
        table.addCell(cell);
    }

    private static void addTableCell(PdfPTable table, String text, boolean center) {
        PdfPCell cell = new PdfPCell(new Phrase(text, NORMAL_FONT));
        cell.setHorizontalAlignment(center ? Element.ALIGN_CENTER : Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private static void addGPASummary(Document document, List<Grade> grades) throws DocumentException {
        document.add(Chunk.NEWLINE);

        // Calculate GPA
        double totalCredits = 0;
        double totalGradePoints = 0;

        for (Grade grade : grades) {
            int credits = grade.getCredits();
            double gradePoints = grade.getGradePoints();
            totalCredits += credits;
            totalGradePoints += (credits * gradePoints);
        }

        double gpa = totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;
        DecimalFormat df = new DecimalFormat("0.00");

        // Summary table
        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(50);
        summaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        addSummaryRow(summaryTable, "Total Credits:", String.valueOf((int) totalCredits));
        addSummaryRow(summaryTable, "Total Grade Points:", df.format(totalGradePoints));

        // GPA row with highlighting
        PdfPCell labelCell = new PdfPCell(new Phrase("CGPA:", HEADER_FONT));
        labelCell.setBackgroundColor(HEADER_BG_COLOR);
        labelCell.setPhrase(new Phrase("CGPA:", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE)));
        labelCell.setPadding(8);
        summaryTable.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(df.format(gpa), HEADER_FONT));
        valueCell.setBackgroundColor(LIGHT_GRAY);
        valueCell.setPadding(8);
        valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        summaryTable.addCell(valueCell);

        document.add(summaryTable);
    }

    private static void addSummaryRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, NORMAL_FONT));
        labelCell.setBackgroundColor(LIGHT_GRAY);
        labelCell.setPadding(5);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, NORMAL_FONT));
        valueCell.setPadding(5);
        valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(valueCell);
    }

    private static void addFooter(Document document) throws DocumentException {
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        LineSeparator separator = new LineSeparator();
        separator.setLineColor(BaseColor.GRAY);
        document.add(new Chunk(separator));

        Paragraph footer = new Paragraph(
                "This is a computer-generated transcript. " +
                "Generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()),
                SMALL_FONT
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(10);
        document.add(footer);

        Paragraph disclaimer = new Paragraph(
                "For official purposes, please obtain a signed copy from the Registrar's Office.",
                SMALL_FONT
        );
        disclaimer.setAlignment(Element.ALIGN_CENTER);
        document.add(disclaimer);
    }
}
