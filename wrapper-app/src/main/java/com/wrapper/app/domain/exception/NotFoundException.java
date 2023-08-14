package com.wrapper.app.domain.exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String resource) {
        super(resource + " not found!");
    }
}
