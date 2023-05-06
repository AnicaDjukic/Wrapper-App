package com.wrapper.app.exception;

public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(String resource) {
        super(resource + " already exists!");
    }
}
