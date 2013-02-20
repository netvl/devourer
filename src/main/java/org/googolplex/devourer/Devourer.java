package org.googolplex.devourer;

import org.googolplex.devourer.configuration.DevourerConfig;
import org.googolplex.devourer.configuration.annotated.MappingReflector;
import org.googolplex.devourer.configuration.modular.MappingModule;
import org.googolplex.devourer.configuration.modular.binders.MappingBinder;
import org.googolplex.devourer.configuration.modular.binders.MappingBinderImpl;
import org.googolplex.devourer.contexts.AttributesContext;
import org.googolplex.devourer.contexts.DefaultAttributesContext;
import org.googolplex.devourer.paths.Path;
import org.googolplex.devourer.paths.PathMapping;
import org.googolplex.devourer.configuration.reactions.ReactionAfter;
import org.googolplex.devourer.configuration.reactions.ReactionAt;
import org.googolplex.devourer.configuration.reactions.ReactionBefore;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/**
 * Devourer pulls XML from the given source and performs preconfigured actions on it. These actions are defined
 * using either {@link MappingModule} instance or annotated configuration. Devourer produces {@link Stacks}
 * instance which contains all output produced by actions.
 *
 * <p>Most implementations of StAX {@link XMLInputFactory} seem to be thread-safe after their configuration,
 * so it is possible to use Devourer instance across multiple threads simultaneously, that is,
 * Devourer should be thread-safe.</p>
 */
public class Devourer {
    private final DevourerConfig config;
    private final XMLInputFactory inputFactory;
    private final Map<Path, PathMapping> mappings;

    protected Devourer(DevourerConfig config, XMLInputFactory inputFactory, Map<Path, PathMapping> mappings) {
        this.config = config;
        this.inputFactory = inputFactory;
        this.mappings = mappings;
    }

    /**
     * Creates new {@link Devourer} with actions defined in the provided {@link MappingModule} using
     * default configuration.
     *
     * @param module mapping module with configured actions
     * @return new Devourer instance
     */
    public static Devourer create(MappingModule module) {
        return create(DevourerConfig.builder().build(), module);
    }

    /**
     * Creates new {@link Devourer} with actions defined in the provided {@link MappingModule} using
     * specified configuration.
     *
     * @param devourerConfig configuration object
     * @param module mapping module with configured actions
     * @return new Devourer instance
     */
    public static Devourer create(DevourerConfig devourerConfig, MappingModule module) {
        MappingBinder binder = new MappingBinderImpl();
        module.configure(binder);
        Map<Path, PathMapping> mappings = binder.mappings();

        return new Devourer(devourerConfig, createXMLInputFactory(devourerConfig), mappings);
    }

    /**
     * Creates new {@link Devourer} with actions defined in the annotated class of the provided object using
     * default configuration.
     *
     * @param configObject object of a class with annotated configuration
     * @return new Devourer instance
     */
    public static Devourer create(Object configObject) {
        return create(DevourerConfig.builder().build(), configObject);
    }

    /**
     * Creates new {@link Devourer} with actions defined in the annotated class of the provided object using
     * default configuration.
     *
     * @param devourerConfig configuration object
     * @param configObject object of a class with annotated configuration
     * @return new Devourer instance
     */
    public static Devourer create(DevourerConfig devourerConfig, Object configObject) {
        MappingReflector reflector = new MappingReflector();
        Map<Path, PathMapping> mappings = reflector.collectMappings(configObject);

        return new Devourer(devourerConfig, createXMLInputFactory(devourerConfig), mappings);
    }

    private static XMLInputFactory createXMLInputFactory(DevourerConfig devourerConfig) {
        // Create input factory and configure it
        XMLInputFactory inputFactory = XMLInputFactory.newFactory();
        for (Map.Entry<String, String> entry : devourerConfig.staxConfig.entrySet()) {
            inputFactory.setProperty(entry.getKey(), entry.getValue());
        }

        return inputFactory;
    }

    /**
     * Parses an XML document contained inside the given string.
     *
     * @param string a string with XML document
     * @return stacks object with parsing results
     * @throws XMLStreamException in case of XML parsing errors
     */
    public Stacks parse(String string) throws XMLStreamException {
        return parse(new StringReader(string));
    }

    /**
     * Parses an XML document contained inside the given array of bytes. The bytes are considered to contain a text
     * encoded using {@code charset} encoding.
     *
     * @param bytes byte array containing an XML document
     * @param charset an encoding of the of the text inside byte array
     * @return stacks object with parsing results
     * @throws XMLStreamException in case of XML parsing errors
     */
    public Stacks parse(byte[] bytes, Charset charset) throws XMLStreamException {
        return parse(new ByteArrayInputStream(bytes), charset);
    }

    /**
     * Parses an XML document contained inside the given array of bytes. The bytes array is considered to contain
     * a text encoded using default encoding.
     *
     * @param bytes byte array containing an XML document
     * @return stacks object with parsing results
     * @throws XMLStreamException in case of XML parsing errors
     */
    public Stacks parse(byte[] bytes) throws XMLStreamException {
        return parse(bytes, Charset.defaultCharset());
    }

    /**
     * Parses an XML document contained within the given input stream. The stream is considered to be encoded using
     * {@code charset} encoding.
     *
     * @param inputStream input stream containing an XML document
     * @param charset an encoding of the input stream
     * @return stacks object with parsing results
     * @throws XMLStreamException in case of XML parsing errors
     */
    public Stacks parse(InputStream inputStream, Charset charset) throws XMLStreamException {
        return parse(new InputStreamReader(inputStream, charset));
    }

    /**
     * Parses an XML document contained within the given input stream. The stream is considered to be encoded using
     * default encoding.
     *
     * @param inputStream input stream containing an XML document
     * @return stacks object with parsing results
     * @throws XMLStreamException in case of XML parsing errors
     */
    public Stacks parse(InputStream inputStream) throws XMLStreamException {
        return parse(inputStream, Charset.defaultCharset());
    }

    /**
     * Parses and XML document contained within the given reader.
     *
     * @param reader reader containing an XML document
     * @return stacks objects with parsing results
     * @throws XMLStreamException in case of XML parsing errors
     */
    public Stacks parse(Reader reader) throws XMLStreamException {
        XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);

        Deque<AttributesContext> contextStack = new ArrayDeque<AttributesContext>();
        Stacks stacks = new DefaultStacks();
        Path currentPath = Path.fromString("/");

        while (streamReader.hasNext()) {
            streamReader.next();  // We will ignore exact event value in favor of reader methods

            if (streamReader.isStartElement()) {
                currentPath = currentPath.resolve(streamReader.getLocalName());

                AttributesContext context = collectAttributesContext(streamReader);
                contextStack.push(context);

                PathMapping mapping = mappings.get(currentPath);
                if (mapping != null) {
                    for (ReactionBefore reaction : mapping.befores) {
                        reaction.react(stacks, context);
                    }
                }

            } else if (streamReader.isCharacters() && !streamReader.isWhiteSpace()) {
                String body = streamReader.getText();
                if (config.stripSpaces) {
                    body = body.trim();
                }
                AttributesContext context = contextStack.peek();

                PathMapping mapping = mappings.get(currentPath);
                if (mapping != null) {
                    for (ReactionAt reaction : mapping.ats) {
                        reaction.react(stacks, context, body);
                    }
                }

            } else if (streamReader.isEndElement()) {
                AttributesContext context = contextStack.pop();

                PathMapping mapping = mappings.get(currentPath);
                if (mapping != null) {
                    for (ReactionAfter reaction : mapping.afters) {
                        reaction.react(stacks, context);
                    }
                }

                currentPath = currentPath.moveUp();
            }
        }

        streamReader.close();

        return stacks;
    }

    private AttributesContext collectAttributesContext(XMLStreamReader reader) {
        DefaultAttributesContext.Builder builder = new DefaultAttributesContext.Builder();

        QName elementName = reader.getName();
        builder.setName(elementName);

        for (int i = 0; i < reader.getAttributeCount(); ++i) {
            QName attributeName = reader.getAttributeName(i);
            builder.addAttribute(attributeName, reader.getAttributeValue(i));
        }

        return builder.build();
    }
}
