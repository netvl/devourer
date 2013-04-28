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

package org.bitbucket.googolplex.devourer.contexts.namespaces;

import com.google.common.base.Optional;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Contains factory methods for creating {@link QualifiedName}s of different structure.
 */
public final class QualifiedNames {
    private QualifiedNames() {
    }

    /**
     * Creates new {@link QualifiedName} without a namespace.
     *
     * @param localName local name, cannot be null or empty
     * @return new qualified name
     */
    public static QualifiedName localOnly(String localName) {
        checkNotNull(localName, "Local name is null");
        checkArgument(!localName.isEmpty(), "Local name is empty");

        return new QualifiedName(localName, Optional.<String>absent(), Optional.<String>absent());
    }

    /**
     * Creates new {@link QualifiedName} with the given namespace and without prefix.
     *
     * @param localName local name, cannot be null or empty
     * @param namespace namespace, cannot be null or empty; namespace URI is not validated as a proper URI
     * @return new qualified name
     */
    public static QualifiedName withNamespace(String localName, String namespace) {
        checkNotNull(localName, "Local name is null");
        checkNotNull(namespace, "Namespace is null");
        checkArgument(!localName.isEmpty(), "Local name is empty");
        checkArgument(!namespace.isEmpty(), "Namespace is empty");

        return new QualifiedName(localName, Optional.of(namespace), Optional.<String>absent());
    }

    /**
     * Creates new {@link QualifiedName} with the given namespace and with prefix.
     *
     * @param localName local name, cannot be null or empty
     * @param namespace namespace, cannot be null or empty; namespace URI is not validated as a proper URI
     * @param prefix prefix, cannot be null or empty
     * @return new qualified name
     */
    public static QualifiedName full(String localName, String namespace, String prefix) {
        checkNotNull(localName, "Local name is null");
        checkNotNull(namespace, "Namespace is null");
        checkNotNull(prefix, "Prefix is null");
        checkArgument(!localName.isEmpty(), "Local name is empty");
        checkArgument(!namespace.isEmpty(), "Namespace is empty");
        checkArgument(!prefix.isEmpty(), "Prefix is empty");

        return new QualifiedName(localName, Optional.of(namespace), Optional.of(prefix));
    }

    /**
     * Creates new {@link QualifiedName} based on the given {@link QName}. The resulting {@link QualifiedName}
     * namespace and prefix properties will be correctly retrieved from the {@link QName}.
     *
     * @param qName standard qualified name object
     * @return new qualified name
     */
    public static QualifiedName fromQName(QName qName) {
        checkNotNull(qName, "Qualified name is null");

        if (XMLConstants.NULL_NS_URI.equals(qName.getNamespaceURI())) {
            return localOnly(qName.getLocalPart());
        } else if (XMLConstants.DEFAULT_NS_PREFIX.equals(qName.getPrefix())) {
            return withNamespace(qName.getLocalPart(), qName.getNamespaceURI());
        } else {
            return full(qName.getLocalPart(), qName.getNamespaceURI(), qName.getPrefix());
        }
    }

    /**
     * Creates new qualified name from a string. The string pattern is as follows:
     * <pre>
     *     ['{' namespace '}'][prefix ':']local-name
     * </pre>
     * Square brackets denote optional parts, quoted characters mean literal values, {@code namespace},
     * {@code prefix} and {@code local-name} mean non-empty strings. There is also a condition: prefix cannot
     * be present without namespace being present too. For example, the following are all valid names:
     * <pre>
     *     local-name
     *     {urn:namespace}local-name
     *     {urn:namespace}n:local-name
     * </pre>
     *
     * @param string a string representing qualified name
     * @return new qualified name
     */
    public static QualifiedName fromString(String string) {
        checkNotNull(string, "String is null");
        checkArgument(!string.isEmpty(), "String is empty");

        // Remove side spaces
        string = string.trim();

        String localName;
        Optional<String> namespace = Optional.absent();
        Optional<String> prefix = Optional.absent();

        // Extract namespace
        int idx = 0;
        if (string.startsWith("{")) {
            int lastIdx = string.indexOf("}");
            if (lastIdx < 0) {
                throw new IllegalArgumentException("Invalid name: found opening '{' but no closing '}'");
            }
            namespace = Optional.of(string.substring(idx + 1, lastIdx));
            idx = lastIdx+1;
        }
        if (idx >= string.length()) {
            throw new IllegalArgumentException("Incomplete name: only namespace is present");
        }
        if (namespace.isPresent() && namespace.get().isEmpty()) {
            throw new IllegalArgumentException("Namespace specified in the name is empty; should leave off '{}' if " +
                                               "no namespace is needed");
        }

        // Extract prefix
        int colonIdx = string.indexOf(':', idx);
        if (colonIdx > 0) {
            prefix = Optional.of(string.substring(idx, colonIdx));
            idx = colonIdx + 1;
        }
        if (idx >= string.length()) {
            if (namespace.isPresent() && prefix.isPresent()) {
                throw new IllegalArgumentException("Incomplete name: namespace and prefix are present, local name " +
                                                   "is missing");
            }
        }
        if (prefix.isPresent()) {
            if (!namespace.isPresent()) {
                throw new IllegalArgumentException("Prefix is specified but namespace is not, cannot construct " +
                                                   "qualified name");
            }

            if (prefix.get().isEmpty()) {
                throw new IllegalArgumentException("Prefix separator is specified but no prefix is present; should " +
                                                   "leave off ':' if no prefix is needed");
            }
        }

        // Extract local name
        localName = string.substring(idx);

        // Create appropriate QualifiedName
        if (namespace.isPresent()) {
            if (prefix.isPresent()) {
                return full(localName, namespace.get(), prefix.get());
            } else {
                return withNamespace(localName, namespace.get());
            }
        } else {
            return localOnly(localName);
        }
    }
}
