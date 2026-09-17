package com.studentms.exception;

/**
 * Custom unchecked exception for invalid student data
 * (e.g., negative age, empty name, out-of-range grade, duplicate ID).
 */
public class InvalidStudentDataException extends RuntimeException {

    public InvalidStudentDataException(String message) {
        super(message);
    }
}
