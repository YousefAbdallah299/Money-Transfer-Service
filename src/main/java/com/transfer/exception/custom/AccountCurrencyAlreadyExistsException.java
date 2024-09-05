package com.transfer.exception.custom;

public class AccountCurrencyAlreadyExistsException extends RuntimeException{
    public AccountCurrencyAlreadyExistsException(String message){
        super(message);
    }
}
