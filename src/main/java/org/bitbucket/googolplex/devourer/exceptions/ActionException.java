package org.bitbucket.googolplex.devourer.exceptions;

/**
 * Wraps an exception which has been thrown from reaction code.
 */
public class ActionException extends DevourerException {
    public ActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
