package com.transfer.exception;

import com.transfer.exception.custom.AccountCurrencyAlreadyExistsException;
import com.transfer.exception.custom.EmailAlreadyExistsException;
import com.transfer.exception.custom.InsufficientFundsException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.exception.response.ErrorDetails;
import com.transfer.exception.response.ValidationFailedResponse;
import com.transfer.exception.response.ViolationErrors;
import io.jsonwebtoken.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class TransferServiceExceptionHandler {
    @ExceptionHandler(AccountCurrencyAlreadyExistsException.class)
    public ResponseEntity<Object> accountCurrencyAlreadyExistsExceptionHandling(AccountCurrencyAlreadyExistsException exception, WebRequest request){
        return new ResponseEntity<>(new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundExceptionHandling(ResourceNotFoundException exception, WebRequest request){
        return new ResponseEntity<>(new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false), HttpStatus.NOT_FOUND),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> emailAlreadyExistsExceptionHandling(EmailAlreadyExistsException exception, WebRequest request) {
        return new ResponseEntity<>(new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Object> InsufficientFundsExceptionHandling(InsufficientFundsException exception, WebRequest request){
        return new ResponseEntity<>(new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }
//IOException

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> InsufficientFundsExceptionHandling(IOException exception, WebRequest request){
        return new ResponseEntity<>(new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> runtimeExceptionHandling(MethodArgumentNotValidException exception){

        ValidationFailedResponse error = ValidationFailedResponse
                .builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .localDateTime(LocalDateTime.now())
                .build();

        for(FieldError fieldError : exception.getBindingResult().getFieldErrors()){
            error.getViolationErrosList().add(
                    ViolationErrors.builder()
                            .fieldName(fieldError.getField())
                            .message(fieldError.getDefaultMessage())
                            .build()
            );
        }
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> globalExceptionHandling(Exception exception, WebRequest request){
        return new ResponseEntity<>(new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> runtimeExceptionHandling(RuntimeException exception, WebRequest request){
        return new ResponseEntity<>(new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> authenticationExceptionHandling(AuthenticationException exception, WebRequest request) {
        return new ResponseEntity<>(new ErrorDetails(LocalDateTime.now()
                , String.format("Incorrect email or password credentials provided. [ %s ]", exception.getMessage()),
                request.getDescription(false), HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    }

}
