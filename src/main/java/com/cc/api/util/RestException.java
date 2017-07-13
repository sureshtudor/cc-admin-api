package com.cc.api.util;

public class RestException extends Exception {

    private static final long serialVersionUID = 1L;
    private String errorMessage;

    public RestException() {
        super();
    }

    public RestException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
