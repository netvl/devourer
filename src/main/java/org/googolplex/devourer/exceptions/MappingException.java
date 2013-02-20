package org.googolplex.devourer.exceptions;

/**
 * An exception which has occured during mapping configuration.
 */
public class MappingException extends DevourerException {
    public MappingException(String message) {
        super(message);
    }

    public MappingException(String message, RuntimeException cause) {
        super(message, cause);
    }
}
