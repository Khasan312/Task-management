package com.example.taskmanagement.exception;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String msg) {
        super(msg);
    }

    public AccessDeniedException() {
        super();
    }
}
