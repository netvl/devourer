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

package org.bitbucket.googolplex.devourer.contexts;

import com.google.common.base.Optional;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedName;

import java.util.Collection;

/**
 * Element context contains information about the current element. It is provided to actions configured
 * in {@link org.bitbucket.googolplex.devourer.configuration.modular.MappingModule}s or to annotated methods in
 * annotated configuration object.
 *
 * <p>Element context is transient; you should never hold instances of this interface neither in
 * {@link org.bitbucket.googolplex.devourer.stacks.Stacks} nor somewhere else.</p>
 *
 * <h3>Several notes regarding namespace contexts</h3>
 *
 * <p>First of all we define what namespace context is. <strong>Namespace context</strong> is a mapping between
 * <strong>namespace URIs</strong> and <strong>its prefixes</strong>. Namespace URI is a unique identifier
 * of a namespace. Prefix is a kind of reference to the namespace URI. Prefix is needed because it is unfeasible
 * to use full namespace URI everywhere where a reference to the namespace is required.</p>
 *
 * <p>However, mapping from namespaces to prefixes is not fixed; it depends on the producer of the document
 * which prefixes will correspond to which namespaces. It is even possible to use the same prefix for
 * different namespaces inside the same document! This can happen if these namespaces are used in disjoint
 * subtrees. But when writing a code for processing XML documents with namespaces almost always the programmer
 * needs to differentiate between namespaces, e.g. filter out all elements which belong to unwanted namespaces.
 * Of course, it is possible to always use full namespace URIs. But this will lead to completely uncomprehensible
 * parsing rules. Ideally it should be possible to have some fixed mapping between namespaces and prefixes
 * and use it to perform element name matches. This is exactly the thing which Devourer allows.</p>
 *
 * <p>Devourer defines two kinds of namespace context. First, there is always a <strong>real</strong> namespace
 * context which dynamically reflects the state of namespaces and their bindings to prefixes at the current position
 * inside the XML document. This context is provided by StAX parser and can be inspected inside actions using
 * {@link #realNamespaceContext()} method. This context is free to change when the position in the document
 * advances (this is why the whole attribute context is transient).</p>
 *
 * <p>The real context is not some constant, unchanging entity. It may change during the processing of the same
 * document or between processings different documents (which happen to have different namespace-prefix mappings). But
 * in order to efficiently parse complex documents with namespaces the programmer needs a way to correctly
 * identify namespaces regardless of the exact prefixes used in the document, while not losing an ability
 * to write terse and understandable matching rules.</p>
 *
 * <p>In order to help with this problem Devourer allows specifying <strong>user-defined</strong> namespace context.
 * This context can be defined by the user and is specified inside
 * {@link org.bitbucket.googolplex.devourer.configuration.modular.MappingModule modular} or
 * {@link org.bitbucket.googolplex.devourer.configuration.annotated.MappingReflector annotated} configuration.
 * This context will be used for resolution of paths for the matching rules (see
 * {@link org.bitbucket.googolplex.devourer.configuration.modular.binders.MappingBinder#on(String)} and also it is
 * used to resolve attribute namespaces by {@link #attribute(QualifiedName)}, {@link #attribute(String, String)}
 * and {@link #attributeWithPrefix(String, String)} methods. You can also get this custom context via
 * {@link #customNamespaceContext()} method.</p>
 *
 * <p>In short, when the user defines custom namespace context</p>
 *
 * @see org.bitbucket.googolplex.devourer.Devourer
 * @see org.bitbucket.googolplex.devourer.configuration.modular.MappingModule
 * @see org.bitbucket.googolplex.devourer.configuration.annotated.MappingReflector
 * @see NamespaceContext
 * @see QualifiedName
 * @see javax.xml.namespace.NamespaceContext
 */
public interface ElementContext {
    /**
     * @return qualified name of the current element
     */
    QualifiedName elementName();

    /**
     * @return current real namespace context, that is, namespace context provided by StAX parser
     */
    javax.xml.namespace.NamespaceContext realNamespaceContext();

    /**
     * @return preconfigured user-defined namespace context
     */
    NamespaceContext customNamespaceContext();

    /**
     * @return a collection of all available attribute names
     */
    Collection<QualifiedName> attributeNames();

    /**
     * Returns an attribute with given local name and no namespace. Note that this method will return
     * absent value if there is some default namespace is set; use either {@link #attribute(String, String)} method or
     * {@link #attribute(QualifiedName)} method or {@link #attributeWithPrefix(String, String)}
     * method with its second argument equal to the prefix configured to retrieve
     * an attribute with no prefix within the default namespace.
     *
     * @param localName local name of the attribute
     * @return an attribute with {@code name} local name and without a namespace, if present
     */
    Optional<String> attribute(String localName);

    /**
     * Returns an attribute with given local name and namespace.
     *
     * @param localName local name of the attribute
     * @param namespace namespace of the attribute
     * @return an attribute with {@code name} local name and {@code prefix} namespace, if present
     */
    Optional<String> attribute(String localName, String namespace);

    /**
     * Returns an attribute with given local name and namespace prefix. The prefix is resolved against the real
     * current namespace context. You can specify {@link javax.xml.XMLConstants#DEFAULT_NS_PREFIX}
     * as the second argument if you need to retrieve an attribute within the default namespace.
     *
     * @param localName local name of the attribute
     * @param prefix namespace of the attribute
     * @return an attribute with {@code name} local name and {@code prefix} namespace prefix, if present
     */
    Optional<String> attributeWithPrefix(String localName, String prefix);

    /**
     * Returns an attribute with given qualified name.
     *
     * @param name qualified name of the attribute
     * @return an attribute with {@code name} full name, if present
     */
    Optional<String> attribute(QualifiedName name);
}
