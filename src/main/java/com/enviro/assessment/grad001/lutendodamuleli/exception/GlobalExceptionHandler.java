package com.enviro.assessment.grad001.lutendodamuleli.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

import static org.springframework.http.HttpStatus.NOT_FOUND;

//This will be the special bean that spring is going to use to handle exceptions
//It's an interceptor
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvestorNotFoundException.class)
    public ResponseEntity<ErrorObject> handleInvestorNotFoundException(InvestorNotFoundException ex, WebRequest request) {
        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(NOT_FOUND.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimeStamp(new Date());

        return new ResponseEntity<ErrorObject>(errorObject, NOT_FOUND);
    }
}
