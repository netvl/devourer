package org.googolplex.devourer.exceptions;

/**
 * Top class of Devourer exception hierarchy.
 */
public class DevourerException extends RuntimeException {
    public DevourerException(String message) {
        super(message);
    }

    public DevourerException(String message, Throwable cause) {
        super(message, cause);
    }
}
