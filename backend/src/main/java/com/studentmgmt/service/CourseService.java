package com.studentmgmt.service;

import com.studentmgmt.dto.CourseDto;
import com.studentmgmt.model.Course;
import com.studentmgmt.model.Faculty;
import com.studentmgmt.repository.CourseRepository;
import com.studentmgmt.repository.FacultyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final FacultyRepository facultyRepository;

    public CourseService(CourseRepository courseRepository, FacultyRepository facultyRepository) {
        this.courseRepository = courseRepository;
        this.facultyRepository = facultyRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    @Transactional
    public Course createCourse(CourseDto dto) {
        if (courseRepository.existsByCourseCode(dto.getCourseCode())) {
            throw new RuntimeException("Course code already exists: " + dto.getCourseCode());
        }

        Course course = new Course();
        course.setCourseCode(dto.getCourseCode());
        course.setCourseName(dto.getCourseName());
        course.setCredits(dto.getCredits());

        if (dto.getFacultyId() != null) {
            Faculty faculty = facultyRepository.findById(dto.getFacultyId())
                    .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + dto.getFacultyId()));
            course.setFaculty(faculty);
        }

        return courseRepository.save(course);
    }

    @Transactional
    public Course updateCourse(Long id, CourseDto dto) {
        Course course = getCourseById(id);
        course.setCourseName(dto.getCourseName());
        course.setCredits(dto.getCredits());

        if (dto.getCourseCode() != null) {
            course.setCourseCode(dto.getCourseCode());
        }

        if (dto.getFacultyId() != null) {
            Faculty faculty = facultyRepository.findById(dto.getFacultyId())
                    .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + dto.getFacultyId()));
            course.setFaculty(faculty);
        }

        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }

    public CourseDto toDto(Course course) {
        CourseDto dto = new CourseDto();
        dto.setCourseId(course.getCourseId());
        dto.setCourseCode(course.getCourseCode());
        dto.setCourseName(course.getCourseName());
        dto.setCredits(course.getCredits());
        if (course.getFaculty() != null) {
            dto.setFacultyId(course.getFaculty().getFacultyId());
            dto.setFacultyName(course.getFaculty().getName());
        }
        return dto;
    }
}
