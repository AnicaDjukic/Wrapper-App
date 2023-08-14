package com.wrapper.app.domain.exception;

public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(String resource) {
        super(resource + " already exists!");
    }
}
