package com.transfer.exception.custom;

public class SameAsOldPasswordException extends RuntimeException{
    public SameAsOldPasswordException(String message){
        super(message);
    }
}
