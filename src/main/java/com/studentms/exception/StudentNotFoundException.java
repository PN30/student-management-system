package com.studentms.exception;

/**
 * Custom checked exception thrown when a student ID does not exist.
 */
public class StudentNotFoundException extends Exception {

    public StudentNotFoundException(String message) {
        super(message);
    }

    public StudentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
