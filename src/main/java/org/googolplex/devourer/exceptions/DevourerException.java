package org.googolplex.devourer.exceptions;

/**
 * Date: 19.02.13
 * Time: 19:57
 *
 * @author Vladimir Matveev
 */
public class DevourerException extends RuntimeException {
    public DevourerException() {
    }

    public DevourerException(String message) {
        super(message);
    }

    public DevourerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DevourerException(Throwable cause) {
        super(cause);
    }
}
