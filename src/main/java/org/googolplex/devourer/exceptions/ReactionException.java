package org.googolplex.devourer.exceptions;

/**
 * Wraps an exception which has been thrown from reaction code.
 */
public class ReactionException extends DevourerException {
    public ReactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
