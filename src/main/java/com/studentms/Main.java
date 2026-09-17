package com.studentms;

import com.studentms.exception.InvalidStudentDataException;
import com.studentms.exception.StudentNotFoundException;
import com.studentms.model.Student;
import com.studentms.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final StudentService studentService = new StudentService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        logger.info("Application started.");
        boolean running = true;

        while (running) {
            printMenu();
            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1 -> addStudent();
                    case 2 -> viewStudents();
                    case 3 -> searchStudent();
                    case 4 -> updateStudent();
                    case 5 -> deleteStudent();
                    case 6 -> {
                        running = false;
                        System.out.println("Exiting. Goodbye!");
                    }
                    default -> System.out.println("Invalid choice. Please select 1-6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: please enter a number for the menu choice.");
                logger.warn("Menu input was not a number.");
            } catch (InvalidStudentDataException e) {
                System.out.println("Invalid data: " + e.getMessage());
                logger.warn("Invalid data rejected: {}", e.getMessage());
            } catch (StudentNotFoundException e) {
                System.out.println("Not found: " + e.getMessage());
                logger.warn("Lookup failed: {}", e.getMessage());
            } catch (Exception e) {
                // Catch-all safety net so the console app never crashes unexpectedly
                System.out.println("Unexpected error: " + e.getMessage());
                logger.error("Unexpected error on menu choice {}", choice, e);
            } finally {
                // Runs after every menu action, success or failure - useful for audit-style logging
                logger.debug("Menu action completed. running={}", running);
            }
            System.out.println();
        }
        scanner.close();
        logger.info("Application exited.");
    }

    private static void printMenu() {
        System.out.println("===== Student Management System =====");
        System.out.println("1. Add Student");
        System.out.println("2. View Students");
        System.out.println("3. Search Student");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addStudent() {
        System.out.print("Enter Student ID: ");
        int id = readInt();

        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Age: ");
        int age = readInt();

        System.out.print("Enter Course: ");
        String course = scanner.nextLine().trim();

        System.out.print("Enter Grade (0-100): ");
        double grade = readDouble();

        Student student = new Student(id, name, age, course, grade);
        studentService.addStudent(student);
        System.out.println("Student added successfully.");
    }

    private static void viewStudents() {
        if (studentService.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }
        System.out.println("---- All Students ----");
        for (Student s : studentService.getAllStudents()) {
            System.out.println(s.getDetails());
        }
    }

    private static void searchStudent() throws StudentNotFoundException {
        System.out.print("Enter Student ID to search: ");
        int id = readInt();
        Student student = studentService.searchStudent(id);
        System.out.println("Found: " + student.getDetails());
    }

    private static void updateStudent() throws StudentNotFoundException {
        System.out.print("Enter Student ID to update: ");
        int id = readInt();

        System.out.print("Enter new Course: ");
        String course = scanner.nextLine().trim();

        System.out.print("Enter new Grade (0-100): ");
        double grade = readDouble();

        studentService.updateStudent(id, course, grade);
        System.out.println("Student updated successfully.");
    }

    private static void deleteStudent() throws StudentNotFoundException {
        System.out.print("Enter Student ID to delete: ");
        int id = readInt();
        studentService.deleteStudent(id);
        System.out.println("Student deleted successfully.");
    }

    private static int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new InvalidStudentDataException("Expected a whole number.");
        }
    }

    private static double readDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new InvalidStudentDataException("Expected a numeric value.");
        }
    }
}
