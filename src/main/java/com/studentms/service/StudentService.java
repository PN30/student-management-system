package com.studentms.service;

import com.studentms.exception.InvalidStudentDataException;
import com.studentms.exception.StudentNotFoundException;
import com.studentms.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    // Java Collections Framework used for in-memory storage
    private final Map<Integer, Student> studentRecords = new LinkedHashMap<>();

    public void addStudent(Student student) {
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            logger.warn("Rejected add: empty name for student ID {}", student.getStudentId());
            throw new InvalidStudentDataException("Student name cannot be empty.");
        }
        if (student.getAge() <= 0) {
            logger.warn("Rejected add: invalid age {} for student ID {}", student.getAge(), student.getStudentId());
            throw new InvalidStudentDataException("Student age must be positive.");
        }
        if (student.getGrade() < 0 || student.getGrade() > 100) {
            logger.warn("Rejected add: invalid grade {} for student ID {}", student.getGrade(), student.getStudentId());
            throw new InvalidStudentDataException("Grade must be between 0 and 100.");
        }
        if (studentRecords.containsKey(student.getStudentId())) {
            logger.warn("Rejected add: duplicate student ID {}", student.getStudentId());
            throw new InvalidStudentDataException(
                "A student with ID " + student.getStudentId() + " already exists."
            );
        }
        studentRecords.put(student.getStudentId(), student);
        logger.info("Student added: ID={}", student.getStudentId());
    }

    public Collection<Student> getAllStudents() {
        logger.debug("Fetching all students, count={}", studentRecords.size());
        return studentRecords.values();
    }

    public Student searchStudent(int studentId) throws StudentNotFoundException {
        Student student = studentRecords.get(studentId);
        if (student == null) {
            logger.warn("Search failed: no student with ID {}", studentId);
            throw new StudentNotFoundException("No student found with ID: " + studentId);
        }
        logger.debug("Student found: ID={}", studentId);
        return student;
    }

    public void updateStudent(int studentId, String course, double grade)
            throws StudentNotFoundException {
        Student student = searchStudent(studentId); // reuses lookup + throws if missing
        if (grade < 0 || grade > 100) {
            logger.warn("Rejected update: invalid grade {} for student ID {}", grade, studentId);
            throw new InvalidStudentDataException("Grade must be between 0 and 100.");
        }
        student.setCourse(course);
        student.setGrade(grade);
        logger.info("Student updated: ID={}", studentId);
    }

    public void deleteStudent(int studentId) throws StudentNotFoundException {
        if (!studentRecords.containsKey(studentId)) {
            logger.warn("Delete failed: no student with ID {}", studentId);
            throw new StudentNotFoundException("No student found with ID: " + studentId);
        }
        studentRecords.remove(studentId);
        logger.info("Student deleted: ID={}", studentId);
    }

    public boolean isEmpty() {
        return studentRecords.isEmpty();
    }
}
