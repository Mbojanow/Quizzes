package com.bocian.quizzes.exceptions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessageFactory {

    public static String createEntityObjectWithIdMissingMessage(final Long id, final String objName) {
        return "Failed to find " + objName + " with id " + id;
    }

    public static String createEntityObjectWithIdMissingMessage(final String id, final String objName) {
        return "Failed to find " + objName + " with id " + id;
    }
}
