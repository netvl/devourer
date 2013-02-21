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
 * Date: 19.02.13
 * Time: 13:30
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

    public static PrefixedName simple(String name) {
        return new PrefixedName(Optional.<String>absent(), name);
    }

    public static PrefixedName withPrefix(String prefix, String name) {
        return new PrefixedName(Optional.of(prefix), name);
    }

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
}
