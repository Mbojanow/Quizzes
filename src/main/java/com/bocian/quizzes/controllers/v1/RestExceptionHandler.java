package com.bocian.quizzes.controllers.v1;

import com.bocian.quizzes.exceptions.DbObjectNotFoundException;
import com.bocian.quizzes.exceptions.InvalidRequestException;
import com.bocian.quizzes.exceptions.ObjectNotValidException;
import com.bocian.quizzes.exceptions.RestCallErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(DbObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestCallErrorMessage handleDbObjectNotFoundException(final Exception exception) {
        log.debug("DbObjectNotFoundException occurred. Details: {}", exception);
        return new RestCallErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestCallErrorMessage handleNumberFormatException(final Exception exception, final WebRequest request) {
        log.debug("NumberFormatException occurred. Details: {}", exception);
        final StringBuilder errorMsgBuilder = new StringBuilder(200);
        errorMsgBuilder.append("Failed to convert value to number. Check your request body, requested path: ")
                .append(request.getDescription(false));

        if (!request.getParameterMap().isEmpty()) {
            errorMsgBuilder.append(" and check request parameters: ");
            request.getParameterMap().forEach((paramName, paramValue)
                    -> errorMsgBuilder.append(paramName).append(": ").append(Arrays.toString(paramValue))
                    .append(" "));
        }

        return new RestCallErrorMessage(errorMsgBuilder.toString());
    }

    @ExceptionHandler(ObjectNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestCallErrorMessage handleObjectNotValidException(final Exception exception) {
        log.debug("ObjectNotValidException occurred. Details: {}", exception);
        return new RestCallErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestCallErrorMessage handleInvalidRequestException(final Exception exception) {
        log.debug("InvalidRequestException. Details: {}", exception);
        return new RestCallErrorMessage("Invalid request. " + exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestCallErrorMessage handleIllegalArgumentException(final Exception exception) {
        log.debug("IllegalArgumentException. Details: {}", exception);
        return new RestCallErrorMessage("Invalid argument in request. " + exception.getMessage());
    }
}
