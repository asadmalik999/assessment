package com.assessment.springboot.exception;

public class ArgumentNotValidException extends Exception{
    public ArgumentNotValidException(String message) {
        super(message);
    }
}
