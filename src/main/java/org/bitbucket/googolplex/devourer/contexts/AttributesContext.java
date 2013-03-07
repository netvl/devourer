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

import javax.xml.namespace.NamespaceContext;
import java.util.Collection;

/**
 * An extension of {@link ElementContext} which adds attribute accessing methods.
 */
public interface AttributesContext extends ElementContext {
    /**
     * @return current namespace context
     */
    NamespaceContext namespaceContext();

    /**
     * @return a collection of all available attribute names
     */
    Collection<QualifiedName> attributeNames();

    /**
     * Returns an attribute with given local name and no namespace.
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
     * Returns an attribute with given local name and namespace prefix. The prefix is resolved against
     * current {@link NamespaceContext}.
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
