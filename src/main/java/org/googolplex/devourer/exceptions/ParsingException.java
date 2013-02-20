package org.googolplex.devourer.exceptions;

/**
 * An exception which has occured during XML parsing. Cause is likely to be {@link javax.xml.stream.XMLStreamException}.
 */
public class ParsingException extends DevourerException {
    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
