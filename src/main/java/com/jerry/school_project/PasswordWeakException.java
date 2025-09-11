package com.jerry.school_project;

public class PasswordWeakException extends RuntimeException {
    public PasswordWeakException(String message) {
        super(message);
    }

    public PasswordWeakException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordWeakException(Throwable cause) {
        super(cause);
    }

    protected PasswordWeakException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
