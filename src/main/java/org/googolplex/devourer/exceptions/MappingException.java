package org.googolplex.devourer.exceptions;

/**
 * Date: 19.02.13
 * Time: 22:20
 *
 * @author Vladimir Matveev
 */
public class MappingException extends DevourerException {
    public MappingException() {
    }

    public MappingException(String message) {
        super(message);
    }

    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MappingException(Throwable cause) {
        super(cause);
    }
}
