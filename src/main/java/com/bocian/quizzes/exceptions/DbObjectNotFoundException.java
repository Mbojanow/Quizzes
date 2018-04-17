package com.bocian.quizzes.exceptions;

public class DbObjectNotFoundException extends Exception {

    public DbObjectNotFoundException() {
        super();
    }

    public DbObjectNotFoundException(final String message) {
        super(message);
    }

    public DbObjectNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
