package org.googolplex.devourer;

import com.sun.xml.internal.fastinfoset.stax.factory.StAXInputFactory;
import org.googolplex.devourer.configuration.DevourerConfig;
import org.googolplex.devourer.configuration.modular.MappingModule;
import org.googolplex.devourer.configuration.modular.binders.MappingBinder;
import org.googolplex.devourer.configuration.modular.binders.MappingBinderImpl;
import org.googolplex.devourer.contexts.AttributesContext;
import org.googolplex.devourer.contexts.DefaultAttributesContext;
import org.googolplex.devourer.paths.Path;
import org.googolplex.devourer.paths.PathMapping;
import org.googolplex.devourer.reactions.ReactionAfter;
import org.googolplex.devourer.reactions.ReactionAt;
import org.googolplex.devourer.reactions.ReactionBefore;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/**
 * Date: 18.02.13
 * Time: 14:52
 */
public class Devourer {
    private final DevourerConfig config;
    private final Map<Path, PathMapping> mappings;

    private Devourer(DevourerConfig config, Map<Path, PathMapping> mappings) {
        this.config = config;
        this.mappings = mappings;
    }

    public static Devourer create(MappingModule module) {
        return create(new DevourerConfig.Builder().build(), module);
    }

    public static Devourer create(DevourerConfig config, MappingModule module) {
        MappingBinder mappingBinder = new MappingBinderImpl();
        module.configure(mappingBinder);
        return new Devourer(config, mappingBinder.mappings());
    }

    public Stacks parse(String string) throws XMLStreamException {
        return parse(new StringReader(string));
    }

    public Stacks parse(InputStream inputStream) throws XMLStreamException {
        return parse(inputStream, Charset.defaultCharset());
    }

    public Stacks parse(InputStream inputStream, Charset charset) throws XMLStreamException {
        return parse(new InputStreamReader(inputStream, charset));
    }

    public Stacks parse(Reader reader) throws XMLStreamException {
        XMLStreamReader streamReader = StAXInputFactory.newFactory().createXMLStreamReader(reader);
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
