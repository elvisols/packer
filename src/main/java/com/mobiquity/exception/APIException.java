package com.mobiquity.exception;

import java.io.IOException;

public class APIException extends IOException {

    public APIException() {
        super();
    }
    public APIException(String message) {
        super(message);
    }
}
