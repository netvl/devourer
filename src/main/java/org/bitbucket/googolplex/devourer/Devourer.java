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

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.bitbucket.googolplex.devourer.configuration.DevourerConfig;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAfter;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAt;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionBefore;
import org.bitbucket.googolplex.devourer.configuration.annotated.MappingReflector;
import org.bitbucket.googolplex.devourer.configuration.modular.MappingModule;
import org.bitbucket.googolplex.devourer.contexts.DefaultElementContext;
import org.bitbucket.googolplex.devourer.contexts.ElementContext;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedName;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedNames;
import org.bitbucket.googolplex.devourer.exceptions.ActionException;
import org.bitbucket.googolplex.devourer.exceptions.DevourerException;
import org.bitbucket.googolplex.devourer.exceptions.ParsingException;
import org.bitbucket.googolplex.devourer.paths.ExactPath;
import org.bitbucket.googolplex.devourer.paths.mappings.ActionBundle;
import org.bitbucket.googolplex.devourer.paths.mappings.PathMapping;
import org.bitbucket.googolplex.devourer.stacks.DefaultStacks;
import org.bitbucket.googolplex.devourer.stacks.Stacks;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Deque;

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
 * configured by the user. Each action is set to be executed on certain <i>path</i> inside the document. A path
 * looks like very simple XPath expression or real path inside the filesystem, e.g. {@code /node/in/document}.
 * Actions can be of three types: <i>before-actions</i>, <i>at-actions</i> and <i>after-actions</i>. Correspondingly,
 * before-actions are executed before Devourer digs deeper into the insides of the current XML node, i.e. when
 * start element is encountered, at-actions are executed when textual body of the current XML node is encountered,
 * and after-actions are executed after whole node has been processed, i.e. when end element is encountered.</p>
 *
 * <p>Actions are represented by interfaces, {@link ActionBefore}, {@link ActionAt} and
 * {@link ActionAfter}. These are functional interfaces (in terms of Java 8 Lambda extension), that is,
 * they consists of single method. In each type this method accepts {@link Stacks} object and {@link ElementContext}
 * object. {@link ActionAt} interface also accepts additional {@link String} parameter.</p>
 *
 * <p>{@link Stacks} object is the main state container for the Devourer; it contains intermediate
 * objects, e.g. builders, and it should contain results of the processing. This object is returned by
 * {@code parse()} family of methods. {@link ElementContext} object contains information about the element
 * currently being processed: name and namespace of the element, as well as its attributes.
 * {@link ActionAt}'s additional {@link String} parameter is set to the body of the element.</p>
 *
 * <p>So, the overall picture of Devourer processing looks as follows. First, you configure a number
 * of actions to be taken on the nodes of expected XML document. Then you ask Devourer to parse
 * the document. Devourer walks through the document and executes the configured actions on each appropriate
 * node. These actions modify {@link Stacks} object based on information provided by the Devourer
 * by the means of {@link ElementContext} object and {@link String} element content. When the whole document
 * has been processed, Devourer returns the {@link Stacks} object used during the processing, which
 * contains results of the actions.</p>
 *
 * <p>See documentation of {@link org.bitbucket.googolplex.devourer.configuration.modular.AbstractMappingModule} and
 * {@link MappingReflector} to find out about mapping configuration details.</p>
 */
public class Devourer {
    private final DevourerConfig config;
    private final XMLInputFactory inputFactory;
    private final PathMapping pathMapping;
    private final NamespaceContext namespaceContext;

    Devourer(DevourerConfig config, XMLInputFactory inputFactory, PathMapping pathMapping,
             NamespaceContext namespaceContext) {
        this.config = config;
        this.inputFactory = inputFactory;
        this.pathMapping = pathMapping;
        this.namespaceContext = namespaceContext;
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
     * Parses an XML document contained inside the given array of bytes. The byte array is considered to contain
     * a text encoded using the specified encoding.
     *
     * @param bytes a byte array containing an XML document
     * @param charset an encoding of the of the text inside the byte array
     * @return stacks object with parsing results
     * @throws DevourerException in case of XML parsing errors or exceptions in actions
     */
    public Stacks parse(byte[] bytes, Charset charset) throws DevourerException {
        Preconditions.checkNotNull(bytes, "Byte array is null");
        Preconditions.checkNotNull(charset, "Charset is null");

        return parse(new ByteArrayInputStream(bytes), charset);
    }

    /**
     * Parses an XML document contained inside the given array of bytes. The byte array is considered to contain
     * a text encoded using default encoding.
     *
     * @param bytes a byte array containing an XML document
     * @return stacks object with parsing results
     * @throws DevourerException in case of XML parsing errors or exceptions in actions
     */
    public Stacks parse(byte[] bytes) throws DevourerException {
        return parse(bytes, Charset.defaultCharset());
    }

    /**
     * Parses an XML document contained within the given input stream. The stream is considered to be encoded using
     * the specified encoding.
     *
     * @param inputStream an input stream containing an XML document
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
     * @param inputStream an input stream containing an XML document
     * @return stacks object with parsing results
     * @throws DevourerException in case of XML parsing errors or exceptions in actions
     */
    public Stacks parse(InputStream inputStream) throws DevourerException {
        return parse(inputStream, Charset.defaultCharset());
    }

    /**
     * Parses and XML document contained within the given reader.
     *
     * @param reader a reader containing an XML document
     * @return stacks objects with parsing results
     * @throws DevourerException in case of XML parsing errors or exceptions in actions
     */
    public Stacks parse(Reader reader) throws DevourerException {
        Preconditions.checkNotNull(reader, "Reader is null");

        XMLStreamReader streamReader = null;
        try {
            streamReader = inputFactory.createXMLStreamReader(reader);

            Deque<ElementContext> contextStack = new ArrayDeque<ElementContext>();
            Stacks stacks = new DefaultStacks();
            ExactPath currentPath = ExactPath.root();

            while (streamReader.hasNext()) {
                streamReader.next();  // We will ignore exact event value in favor of reader methods

                if (streamReader.isStartElement()) {
                    currentPath = currentPath.resolve(QualifiedNames.fromQName(streamReader.getName()));
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

    private void handleStartElement(XMLStreamReader streamReader, ExactPath currentPath, Stacks stacks,
                                    Deque<ElementContext> contextStack) {
        ElementContext context = assembleAttributesContext(streamReader);
        contextStack.push(context);

        Optional<ActionBundle> bundle = pathMapping.lookup(currentPath, namespaceContext);
        if (bundle.isPresent()) {
            for (ActionBefore action : bundle.get().befores) {
                action.act(stacks, context);
            }
        }
    }

    private void handleContent(XMLStreamReader streamReader, ExactPath currentPath, Stacks stacks,
                               Deque<ElementContext> contextStack) {
        String body = streamReader.getText();
        if (config.stripSpaces) {
            body = body.trim();
        }
        ElementContext context = contextStack.peek();

        Optional<ActionBundle> bundle = pathMapping.lookup(currentPath, namespaceContext);
        if (bundle.isPresent()) {
            for (ActionAt action : bundle.get().ats) {
                action.act(stacks, context, body);
            }
        }
    }

    private void handleEndElement(ExactPath currentPath, Stacks stacks, Deque<ElementContext> contextStack) {
        ElementContext context = contextStack.pop();

        Optional<ActionBundle> bundle = pathMapping.lookup(currentPath, namespaceContext);
        if (bundle.isPresent()) {
            for (ActionAfter action : bundle.get().afters) {
                action.act(stacks, context);
            }
        }
    }

    private ElementContext assembleAttributesContext(XMLStreamReader reader) {
        DefaultElementContext.Builder builder = new DefaultElementContext.Builder();

        builder.setRealNamespaceContext(reader.getNamespaceContext());
        builder.setCustomNamespaceContext(namespaceContext);

        QualifiedName elementName = QualifiedNames.fromQName(reader.getName());
        builder.setName(elementName);

        for (int i = 0; i < reader.getAttributeCount(); ++i) {
            QualifiedName attributeName = QualifiedNames.fromQName(reader.getAttributeName(i));
            builder.addAttribute(attributeName, reader.getAttributeValue(i));
        }

        return builder.build();
    }
}
