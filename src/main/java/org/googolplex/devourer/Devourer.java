package org.googolplex.devourer;

import com.sun.xml.internal.fastinfoset.stax.factory.StAXInputFactory;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

/**
 * Date: 18.02.13
 * Time: 14:52
 */
public class Devourer {
    public Stacks stackState() {
        return null;
    }

    public void configure(Object configuration) {

    }

    public void parse(InputStream inputStream) throws XMLStreamException {
        XMLStreamReader reader = StAXInputFactory.newFactory().createXMLStreamReader(inputStream);
    }
}
