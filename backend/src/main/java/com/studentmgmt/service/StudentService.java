package com.studentmgmt.service;

import com.studentmgmt.dto.StudentDto;
import com.studentmgmt.model.Student;
import com.studentmgmt.model.User;
import com.studentmgmt.model.UserRole;
import com.studentmgmt.repository.StudentRepository;
import com.studentmgmt.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    public Student getStudentByUser(User user) {
        return studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student profile not found for user: " + user.getUsername()));
    }

    @Transactional
    public Student createStudent(StudentDto dto) {
        if (dto.getRollNumber() != null && studentRepository.existsByRollNumber(dto.getRollNumber())) {
            throw new RuntimeException("Roll number already exists: " + dto.getRollNumber());
        }

        User user = null;
        if (dto.getUsername() != null && dto.getPassword() != null) {
            if (userRepository.existsByUsername(dto.getUsername())) {
                throw new RuntimeException("Username already exists: " + dto.getUsername());
            }
            user = new User(dto.getUsername(), passwordEncoder.encode(dto.getPassword()), UserRole.STUDENT);
            user = userRepository.save(user);
        }

        Student student = new Student();
        student.setUser(user);
        student.setName(dto.getName());
        student.setRollNumber(dto.getRollNumber());
        student.setEmail(dto.getEmail());
        student.setDepartment(dto.getDepartment());
        student.setSemester(dto.getSemester());

        return studentRepository.save(student);
    }

    @Transactional
    public Student updateStudent(Long id, StudentDto dto) {
        Student student = getStudentById(id);
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setDepartment(dto.getDepartment());
        student.setSemester(dto.getSemester());

        if (dto.getRollNumber() != null) {
            student.setRollNumber(dto.getRollNumber());
        }

        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }

    public StudentDto toDto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setStudentId(student.getStudentId());
        dto.setName(student.getName());
        dto.setRollNumber(student.getRollNumber());
        dto.setEmail(student.getEmail());
        dto.setDepartment(student.getDepartment());
        dto.setSemester(student.getSemester());
        if (student.getUser() != null) {
            dto.setUsername(student.getUser().getUsername());
        }
        return dto;
    }
}
