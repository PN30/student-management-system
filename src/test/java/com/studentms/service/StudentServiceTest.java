package com.studentms.service;

import com.studentms.exception.InvalidStudentDataException;
import com.studentms.exception.StudentNotFoundException;
import com.studentms.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentServiceTest {

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService();
    }

    @Test
    void addStudentStoresStudent() {
        Student student = new Student(1, "Ada Lovelace", 20, "Math", 95.5);

        studentService.addStudent(student);

        assertSame(student, studentService.getAllStudents().iterator().next());
        assertFalse(studentService.isEmpty());
    }

    @Test
    void addStudentAcceptsGradeBoundaries() {
        Student lowestGrade = new Student(1, "Ada", 20, "Math", 0);
        Student highestGrade = new Student(2, "Grace", 21, "Science", 100);

        studentService.addStudent(lowestGrade);
        studentService.addStudent(highestGrade);

        assertEquals(2, studentService.getAllStudents().size());
    }

    @Test
    void addStudentRejectsEmptyName() {
        Student student = new Student(1, "   ", 20, "Math", 80);

        assertThrows(InvalidStudentDataException.class, () -> studentService.addStudent(student));
        assertTrue(studentService.isEmpty());
    }

    @Test
    void addStudentRejectsNonPositiveAge() {
        Student zeroAge = new Student(1, "Ada", 0, "Math", 80);
        Student negativeAge = new Student(2, "Grace", -1, "Science", 80);

        assertThrows(InvalidStudentDataException.class, () -> studentService.addStudent(zeroAge));
        assertThrows(InvalidStudentDataException.class, () -> studentService.addStudent(negativeAge));
        assertTrue(studentService.isEmpty());
    }

    @Test
    void addStudentRejectsGradeOutsideRange() {
        Student belowRange = new Student(1, "Ada", 20, "Math", -0.1);
        Student aboveRange = new Student(2, "Grace", 21, "Science", 100.1);

        assertThrows(InvalidStudentDataException.class, () -> studentService.addStudent(belowRange));
        assertThrows(InvalidStudentDataException.class, () -> studentService.addStudent(aboveRange));
        assertTrue(studentService.isEmpty());
    }

    @Test
    void addStudentRejectsDuplicateIdAndPreservesOriginal() throws StudentNotFoundException {
        Student original = new Student(1, "Ada", 20, "Math", 90);
        Student duplicate = new Student(1, "Grace", 21, "Science", 85);
        studentService.addStudent(original);

        assertThrows(InvalidStudentDataException.class, () -> studentService.addStudent(duplicate));

        assertSame(original, studentService.searchStudent(1));
        assertEquals(1, studentService.getAllStudents().size());
    }

    @Test
    void searchStudentReturnsMatchingStudent() throws StudentNotFoundException {
        Student student = new Student(1, "Ada", 20, "Math", 90);
        studentService.addStudent(student);

        assertSame(student, studentService.searchStudent(1));
    }

    @Test
    void searchStudentThrowsWhenStudentIsMissing() {
        assertThrows(StudentNotFoundException.class, () -> studentService.searchStudent(1));
    }

    @Test
    void updateStudentChangesCourseAndGrade() throws StudentNotFoundException {
        Student student = new Student(1, "Ada", 20, "Math", 90);
        studentService.addStudent(student);

        studentService.updateStudent(1, "Computer Science", 96.5);

        assertEquals("Computer Science", student.getCourse());
        assertEquals(96.5, student.getGrade());
    }

    @Test
    void updateStudentRejectsGradeOutsideRangeWithoutChangingStudent() throws StudentNotFoundException {
        Student student = new Student(1, "Ada", 20, "Math", 90);
        studentService.addStudent(student);

        assertThrows(InvalidStudentDataException.class, () -> studentService.updateStudent(1, "Science", -0.1));
        assertThrows(InvalidStudentDataException.class, () -> studentService.updateStudent(1, "Science", 100.1));

        assertEquals("Math", student.getCourse());
        assertEquals(90, student.getGrade());
    }

    @Test
    void updateStudentThrowsWhenStudentIsMissing() {
        assertThrows(StudentNotFoundException.class, () -> studentService.updateStudent(1, "Science", 90));
    }

    @Test
    void deleteStudentRemovesStudent() throws StudentNotFoundException {
        studentService.addStudent(new Student(1, "Ada", 20, "Math", 90));

        studentService.deleteStudent(1);

        assertTrue(studentService.isEmpty());
    }

    @Test
    void deleteStudentThrowsWhenStudentIsMissing() {
        assertThrows(StudentNotFoundException.class, () -> studentService.deleteStudent(1));
    }

    @Test
    void isEmptyReflectsWhetherStudentsAreStored() throws StudentNotFoundException {
        assertTrue(studentService.isEmpty());

        studentService.addStudent(new Student(1, "Ada", 20, "Math", 90));
        assertFalse(studentService.isEmpty());

        studentService.deleteStudent(1);
        assertTrue(studentService.isEmpty());
    }

    @Test
    void getAllStudentsPreservesInsertionOrder() {
        studentService.addStudent(new Student(3, "C", 20, "Math", 70));
        studentService.addStudent(new Student(1, "A", 20, "Math", 80));
        studentService.addStudent(new Student(2, "B", 20, "Math", 90));

        List<Integer> ids = studentService.getAllStudents().stream()
                .map(Student::getStudentId).toList();

        assertEquals(List.of(3, 1, 2), ids);
    }

    @Test
    void updateStudentAcceptsGradeBoundaries() throws StudentNotFoundException {
        Student student = new Student(1, "Ada", 20, "Math", 90);
        studentService.addStudent(student);

        studentService.updateStudent(1, "Math", 0);
        assertEquals(0, student.getGrade());

        studentService.updateStudent(1, "Math", 100);
        assertEquals(100, student.getGrade());

    }
}
