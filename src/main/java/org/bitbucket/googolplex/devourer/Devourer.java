/*******************************************************************************
 * Copyright 2013 Vladimir Matveev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.bitbucket.googolplex.devourer;

import com.google.common.base.Preconditions;
import org.bitbucket.googolplex.devourer.configuration.DevourerConfig;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAt;
import org.bitbucket.googolplex.devourer.configuration.annotated.MappingReflector;
import org.bitbucket.googolplex.devourer.configuration.modular.MappingModule;
import org.bitbucket.googolplex.devourer.configuration.modular.binders.MappingBinder;
import org.bitbucket.googolplex.devourer.configuration.modular.binders.MappingBinderImpl;
import org.bitbucket.googolplex.devourer.contexts.AttributesContext;
import org.bitbucket.googolplex.devourer.contexts.DefaultAttributesContext;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedName;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedNames;
import org.bitbucket.googolplex.devourer.exceptions.ActionException;
import org.bitbucket.googolplex.devourer.exceptions.DevourerException;
import org.bitbucket.googolplex.devourer.exceptions.MappingException;
import org.bitbucket.googolplex.devourer.exceptions.ParsingException;
import org.bitbucket.googolplex.devourer.paths.SimplePath;
import org.bitbucket.googolplex.devourer.paths.mappings.PathMapping;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAfter;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionBefore;
import org.bitbucket.googolplex.devourer.stacks.DefaultStacks;
import org.bitbucket.googolplex.devourer.stacks.Stacks;

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
 * Devourer pulls XML from the given source and executes preconfigured actions on it. These actions are defined
 * using either {@link MappingModule} instance or annotated configuration. Devourer produces {@link Stacks}
 * instance which contains all output produced by actions.
 *
 * <p>Most implementations of StAX {@link XMLInputFactory} seem to be thread-safe after their configuration,
 * so it is possible to use Devourer instance across multiple threads simultaneously, that is,
 * Devourer should be thread-safe.</p>
 *
 * <p>Devourer parses XML document and executes series of actions on each node it encounters. Concrete actions are
 * configured by the user. Each action is set to be executed on certain <i>path</i> inside the document. SimplePath
 * looks like very simple XPath expression or real path inside the filesystem, e.g. {@code /node/in/document}.
 * Actions can be of three types: <i>before-actions</i>, <i>at-actions</i> and <i>after-actions</i>. Correspondingly,
 * before-actions are executed before Devourer digs deeper into the insides of the current XML node, i.e. when
 * start element is encountered, at-actions are executed when textual body of the current XML node is encountered,
 * and after-actions are executed after whole node has been processed, i.e. when end element is encountered.</p>
 *
 * <p>Actions are represented by interfaces, {@link ActionBefore}, {@link ActionAt} and
 * {@link ActionAfter}. These are functional interfaces (in terms of Java 8 Lambda extension), that is,
 * they consists of single method. In each type this method accepts {@link Stacks} object and {@link AttributesContext}
 * object. {@link ActionAt} interface also accepts additional {@link String} parameter.</p>
 *
 * <p>{@link Stacks} object is the main state container for the Devourer; it contains intermediate
 * objects, e.g. builders, and it should contain results of the processing. This object is returned by
 * {@code parse()} family of methods. {@link AttributesContext} object contains information about the element
 * currently being processed: name and namespace of the element, as well as its attributes.
 * {@link ActionAt}'s additional {@link String} parameter is set to the body of the element.</p>
 *
 * <p>So, the overall picture of Devourer processing looks as follows. First, you configure a number
 * of actions to be taken on the nodes of expected XML document. Then you ask Devourer to parse
 * the document. Devourer walks through the document and executes the configured actions on each appropriate
 * node. These actions modify {@link Stacks} object based on information provided by the Devourer
 * by the means of {@link AttributesContext} object and {@link String} element content. When the whole document
 * has been processed, Devourer returns the {@link Stacks} object used during the processing, which
 * contains results of the actions.</p>
 *
 * <p>See documentation of {@link org.bitbucket.googolplex.devourer.configuration.modular.AbstractMappingModule} and
 * {@link MappingReflector} to find out about mapping configuration details.</p>
 */
public class Devourer {
    private final DevourerConfig config;
    private final XMLInputFactory inputFactory;
    // TODO: maybe it makes sense to switch to a set of three multimaps, one for each of mapping types
    private final Map<SimplePath, PathMapping> mappings;

    protected Devourer(DevourerConfig config, XMLInputFactory inputFactory, Map<SimplePath, PathMapping> mappings) {
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
        Preconditions.checkNotNull(devourerConfig, "Devourer config is null");
        Preconditions.checkNotNull(module, "Module object is null");

        MappingBinder binder = new MappingBinderImpl();
        try {
            module.configure(binder);
        } catch (RuntimeException e) {
            throw new MappingException("An exception occured during mapping module configuration", e);
        }
        Map<SimplePath, PathMapping> mappings = binder.mappings();

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
        Preconditions.checkNotNull(devourerConfig, "Devourer config is null");
        Preconditions.checkNotNull(configObject, "Config object is null");

        MappingReflector reflector = new MappingReflector();
        Map<SimplePath, PathMapping> mappings = reflector.collectMappings(configObject);

        return new Devourer(devourerConfig, createXMLInputFactory(devourerConfig), mappings);
    }

    private static XMLInputFactory createXMLInputFactory(DevourerConfig devourerConfig) {
        // Create input factory and configure it
        XMLInputFactory inputFactory = XMLInputFactory.newFactory();
        for (Map.Entry<String, Object> entry : devourerConfig.staxConfig.entrySet()) {
            inputFactory.setProperty(entry.getKey(), entry.getValue());
        }

        return inputFactory;
    }

    /**
     * Parses an XML document contained inside the given string.
     *
     * @param string a string with XML document
     * @return stacks object with parsing results
     * @throws DevourerException in case of XML parsing errors or exceptions in actions
     */
    public Stacks parse(String string) throws DevourerException {
        Preconditions.checkNotNull(string, "String is null");

        return parse(new StringReader(string));
    }

    /**
     * Parses an XML document contained inside the given array of bytes. The bytes are considered to contain a text
     * encoded using {@code charset} encoding.
     *
     * @param bytes byte array containing an XML document
     * @param charset an encoding of the of the text inside byte array
     * @return stacks object with parsing results
     * @throws DevourerException in case of XML parsing errors or exceptions in actions
     */
    public Stacks parse(byte[] bytes, Charset charset) throws DevourerException {
        Preconditions.checkNotNull(bytes, "Byte array is null");
        Preconditions.checkNotNull(charset, "Charset is null");

        return parse(new ByteArrayInputStream(bytes), charset);
    }

    /**
     * Parses an XML document contained inside the given array of bytes. The bytes array is considered to contain
     * a text encoded using default encoding.
     *
     * @param bytes byte array containing an XML document
     * @return stacks object with parsing results
     * @throws DevourerException in case of XML parsing errors or exceptions in actions
     */
    public Stacks parse(byte[] bytes) throws DevourerException {
        return parse(bytes, Charset.defaultCharset());
    }

    /**
     * Parses an XML document contained within the given input stream. The stream is considered to be encoded using
     * {@code charset} encoding.
     *
     * @param inputStream input stream containing an XML document
     * @param charset an encoding of the input stream
     * @return stacks object with parsing results
     * @throws DevourerException in case of XML parsing errors or exceptions in actions
     */
    public Stacks parse(InputStream inputStream, Charset charset) throws DevourerException {
        Preconditions.checkNotNull(inputStream, "Input stream is null");
        Preconditions.checkNotNull(charset, "Charset is null");

        return parse(new InputStreamReader(inputStream, charset));
    }

    /**
     * Parses an XML document contained within the given input stream. The stream is considered to be encoded using
     * default encoding.
     *
     * @param inputStream input stream containing an XML document
     * @return stacks object with parsing results
     * @throws DevourerException in case of XML parsing errors or exceptions in actions
     */
    public Stacks parse(InputStream inputStream) throws DevourerException {
        return parse(inputStream, Charset.defaultCharset());
    }

    /**
     * Parses and XML document contained within the given reader.
     *
     * @param reader reader containing an XML document
     * @return stacks objects with parsing results
     * @throws DevourerException in case of XML parsing errors or exceptions in actions
     */
    public Stacks parse(Reader reader) throws DevourerException {
        Preconditions.checkNotNull(reader, "Reader is null");

        XMLStreamReader streamReader = null;
        try {
            streamReader = inputFactory.createXMLStreamReader(reader);

            Deque<AttributesContext> contextStack = new ArrayDeque<AttributesContext>();
            Stacks stacks = new DefaultStacks();
            SimplePath currentPath = SimplePath.fromString("");

            while (streamReader.hasNext()) {
                streamReader.next();  // We will ignore exact event value in favor of reader methods

                if (streamReader.isStartElement()) {
                    currentPath = currentPath.resolve(streamReader.getLocalName());
                    handleStartElement(streamReader, currentPath, stacks, contextStack);

                } else if (streamReader.isCharacters() && !streamReader.isWhiteSpace()) {
                    handleContent(streamReader, currentPath, stacks, contextStack);

                } else if (streamReader.isEndElement()) {
                    handleEndElement(currentPath, stacks, contextStack);
                    currentPath = currentPath.moveUp();

                }
            }

            streamReader.close();

            return stacks;

        } catch (XMLStreamException e) {
            throw new ParsingException("Error while parsing XML document", e);

        } catch (RuntimeException e) {  // TODO: maybe move to action loops
            throw new ActionException("An exception has occured in action", e);

        } finally {
            if (streamReader != null) {
                try {
                    streamReader.close();
                } catch (XMLStreamException e) {
                    // Do nothing
                }
            }
        }
    }

    private void handleStartElement(XMLStreamReader streamReader, SimplePath currentPath, Stacks stacks,
                                    Deque<AttributesContext> contextStack) {
        AttributesContext context = collectAttributesContext(streamReader);
        contextStack.push(context);

        PathMapping mapping = mappings.get(currentPath);
        if (mapping != null) {
            for (ActionBefore action : mapping.befores) {
                action.act(stacks, context);
            }
        }
    }

    private void handleContent(XMLStreamReader streamReader, SimplePath currentPath, Stacks stacks,
                               Deque<AttributesContext> contextStack) {
        String body = streamReader.getText();
        if (config.stripSpaces) {
            body = body.trim();
        }
        AttributesContext context = contextStack.peek();

        PathMapping mapping = mappings.get(currentPath);
        if (mapping != null) {
            for (ActionAt action : mapping.ats) {
                action.act(stacks, context, body);
            }
        }
    }

    private void handleEndElement(SimplePath currentPath, Stacks stacks, Deque<AttributesContext> contextStack) {
        AttributesContext context = contextStack.pop();

        PathMapping mapping = mappings.get(currentPath);
        if (mapping != null) {
            for (ActionAfter action : mapping.afters) {
                action.act(stacks, context);
            }
        }
    }

    private AttributesContext collectAttributesContext(XMLStreamReader reader) {
        DefaultAttributesContext.Builder builder = new DefaultAttributesContext.Builder();

        builder.setNamespaceContext(reader.getNamespaceContext());

        QualifiedName elementName = QualifiedNames.fromQName(reader.getName());
        builder.setName(elementName);

        for (int i = 0; i < reader.getAttributeCount(); ++i) {
            QualifiedName attributeName = QualifiedNames.fromQName(reader.getAttributeName(i));
            builder.addAttribute(attributeName, reader.getAttributeValue(i));
        }

        return builder.build();
    }
}
