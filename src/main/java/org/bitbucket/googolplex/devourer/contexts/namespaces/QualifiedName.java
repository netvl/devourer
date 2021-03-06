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
 * Represents qualified name used in XML documents. Qualified name consists of local name, namespace and prefix.
 * Namespace is optional, as well as prefix, but prefix cannot be present without a namespace.
 *
 * <p>This class is very similar to {@link QName} from standard library but provides more convenient interface.
 * It also does not support empty local names, and absence of namespace and prefix is modelled using
 * {@link Optional}s.</p>
 */
public class QualifiedName {
    public final String localName;
    public final Optional<String> namespace;
    public final Optional<String> prefix;

    QualifiedName(String localName, Optional<String> namespace, Optional<String> prefix) {
        checkNotNull(localName, "Local name is null");
        checkNotNull(namespace, "Namespace is null");
        checkNotNull(prefix, "Prefix is null");
        checkArgument(!localName.isEmpty(), "Local name is empty");

        this.localName = localName;
        this.namespace = namespace;
        this.prefix = prefix;
    }

    public QName asQName() {
        return new QName(
            namespace.or(XMLConstants.NULL_NS_URI),
            localName,
            prefix.or(XMLConstants.DEFAULT_NS_PREFIX)
        );
    }

    @Override
    public String toString() {
        String result = localName;
        if (prefix.isPresent()) {
            result = prefix.get() + ":" + result;
        }
        if (namespace.isPresent()) {
            result = "{" + namespace.get() + "}" + result;
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QualifiedName that = (QualifiedName) o;

        return localName.equals(that.localName) && namespace.equals(that.namespace);
    }

    @Override
    public int hashCode() {
        return localName.hashCode() ^ namespace.hashCode();
    }
}
