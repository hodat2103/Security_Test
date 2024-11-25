package com.vsii.coursemanagement.exceptions;

public class XSSServletException extends RuntimeException {

    public XSSServletException() {
    }

    public XSSServletException(String message) {
        super(message);
    }
}
