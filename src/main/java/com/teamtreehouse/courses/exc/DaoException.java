package com.teamtreehouse.courses.exc;

public class DaoException extends Exception {

    private final Exception originalException;

    public DaoException(Exception originalException, String msg) {
        super(msg);
        this.originalException = originalException;
    }
}
