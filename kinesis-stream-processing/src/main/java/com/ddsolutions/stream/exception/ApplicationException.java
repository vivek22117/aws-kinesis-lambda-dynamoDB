package com.ddsolutions.stream.exception;


public class ApplicationException extends RuntimeException {

    public ApplicationException(String message, Throwable cause){
        super(message, cause);
    }
}
