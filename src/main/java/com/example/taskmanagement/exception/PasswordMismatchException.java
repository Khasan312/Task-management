package com.example.taskmanagement.exception;

public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException(String msg) {
        super(msg);
    }

    public PasswordMismatchException() {
        super();
    }
}
