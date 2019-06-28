package com.ddsolutions.stream.exception;

/**
 * Created by Vivek Kumar Mishra on 20-03-2018.
 */
public class ApplicationException extends RuntimeException {

    public ApplicationException(String message, Throwable cause){
        super(message, cause);
    }
}
