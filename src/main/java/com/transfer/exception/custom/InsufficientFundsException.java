package com.transfer.exception.custom;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(String message){
    super(message);
}

}
