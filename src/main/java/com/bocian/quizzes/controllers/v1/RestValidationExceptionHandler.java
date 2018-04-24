package com.bocian.quizzes.controllers.v1;

import com.bocian.quizzes.exceptions.RestCallErrorMessage;
import com.bocian.quizzes.exceptions.RestValidationErrorMessageFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestValidationExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        return new ResponseEntity<>(new RestCallErrorMessage(RestValidationErrorMessageFactory.getDefaultMessage(ex)),
                HttpStatus.BAD_REQUEST);
    }
}
