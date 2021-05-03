package com.mobiquity.exception;

public class APIException extends Exception {

    public APIException() {
        super();
    }

    public APIException(String message) {
        super(message);
    }

    public APIException(Exception e) {
        super(e);
    }

}
