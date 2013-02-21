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

package org.googolplex.devourer.contexts;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * Simple wrapper around XML element's local name and prefix. Has well-defined {@link #equals(Object)} and
 * {@link #hashCode()} methods.
 */
public class PrefixedName {
    public final Optional<String> prefix;
    public final String name;

    public PrefixedName(Optional<String> prefix, String name) {
        Preconditions.checkNotNull(prefix, "Prefix is null");
        Preconditions.checkNotNull(name, "Name is null");

        this.prefix = prefix;
        this.name = name;
    }

    /**
     * Creates prefixed name from the given local name.
     *
     * @param name local name of the element
     * @return prefixed name with no prefix
     */
    public static PrefixedName simple(String name) {
        return new PrefixedName(Optional.<String>absent(), name);
    }

    /**
     * Creates prefixed name from the given prefix and local name.
     *
     * @param prefix prefix of the element name
     * @param name local name of the element
     * @return prefixed name with prefix
     */
    public static PrefixedName withPrefix(String prefix, String name) {
        return new PrefixedName(Optional.of(prefix), name);
    }

    /**
     * Creates prefixed name from {@link QName} instance.
     *
     * @param qName qualified name of the element
     * @return prefixed name
     */
    public static PrefixedName fromQName(QName qName) {
        if (XMLConstants.NULL_NS_URI.equals(qName.getNamespaceURI()) &&
            XMLConstants.DEFAULT_NS_PREFIX.equals(qName.getPrefix())) {
            return PrefixedName.simple(qName.getLocalPart());
        } else {
            return PrefixedName.withPrefix(qName.getPrefix(), qName.getLocalPart());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PrefixedName that = (PrefixedName) o;

        return name.equals(that.name) && prefix.equals(that.prefix);

    }

    @Override
    public int hashCode() {
        int result = prefix.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        if (prefix.isPresent()) {
            return prefix.get() + ":" + name;
        } else {
            return name;
        }
    }
}
