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
import com.google.common.base.Preconditions;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * Date: 07.03.13
 * Time: 22:08
 *
 * @author Vladimir Matveev
 */
public final class QualifiedNames {
    private QualifiedNames() {
    }

    public static QualifiedName localOnly(String localName) {
        Preconditions.checkNotNull(localName, "Local name is null");

        return new QualifiedName(localName, Optional.<String>absent(), Optional.<String>absent());
    }

    public static QualifiedName withNamespace(String localName, String namespace) {
        Preconditions.checkNotNull(localName, "Local name is null");
        Preconditions.checkNotNull(namespace, "Namespace is null");
        Preconditions.checkArgument(!localName.isEmpty(), "Local name is empty");
        Preconditions.checkArgument(!namespace.isEmpty(), "Namespace is empty");

        return new QualifiedName(localName, Optional.of(namespace), Optional.<String>absent());
    }

    public static QualifiedName full(String localName, String namespace, String prefix) {
        Preconditions.checkNotNull(localName, "Local name is null");
        Preconditions.checkNotNull(namespace, "Namespace is null");
        Preconditions.checkNotNull(prefix, "Prefix is null");
        Preconditions.checkArgument(!localName.isEmpty(), "Local name is empty");
        Preconditions.checkArgument(!namespace.isEmpty(), "Namespace is empty");
        Preconditions.checkArgument(!prefix.isEmpty(), "Prefix is empty");

        return new QualifiedName(localName, Optional.of(namespace), Optional.of(prefix));
    }

    public static QualifiedName fromQName(QName qName) {
        Preconditions.checkNotNull(qName, "Qualified name is null");

        if (qName.getNamespaceURI().equals(XMLConstants.NULL_NS_URI)) {
            return localOnly(qName.getLocalPart());
        } else if (qName.getPrefix().equals(XMLConstants.DEFAULT_NS_PREFIX)) {
            return withNamespace(qName.getLocalPart(), qName.getNamespaceURI());
        } else {
            return full(qName.getLocalPart(), qName.getNamespaceURI(), qName.getPrefix());
        }
    }

    public static QualifiedName fromString(String string) {
        Preconditions.checkNotNull(string, "String is null");
        Preconditions.checkArgument(!string.isEmpty(), "String is empty");

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
