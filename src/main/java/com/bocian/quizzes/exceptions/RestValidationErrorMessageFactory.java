package com.bocian.quizzes.exceptions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class RestValidationErrorMessageFactory {

    public static String getDefaultMessage(final MethodArgumentNotValidException exception) {
        if (!exception.getBindingResult().hasErrors()) {
            log.warn("Building message from MethodArgumentNotValidException that has no errors!" +
                    " This might be a logical error");
        }

        final StringBuilder messageBuilder = new StringBuilder();
        exception.getBindingResult().getAllErrors()
                .forEach(objectError -> messageBuilder.append(objectError.getDefaultMessage()).append(". "));
        return messageBuilder.toString().trim();
    }
}
