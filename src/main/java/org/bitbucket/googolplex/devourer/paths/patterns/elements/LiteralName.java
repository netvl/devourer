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

package org.bitbucket.googolplex.devourer.paths.patterns.elements;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedName;
import org.bitbucket.googolplex.devourer.paths.PathsConstants;

import java.util.List;

/**
 * Literal name represents exactly one part of a pattern, possibly a single (non-global) wildcard.
 * <p>
 *     TODO: change name of this class to something more appropriate
 * </p>
 */
public class LiteralName extends PatternElement {
    public final String name;
    public final Optional<String> prefix;

    private LiteralName(String name, Optional<String> prefix) {
        this.name = name;
        this.prefix = prefix;
    }

    @Override
    public boolean isLiteral() {
        return true;
    }

    @Override
    public String toString() {
        if (prefix.isPresent()) {
            return prefix.get() + ":" + name;
        } else {
            return name;
        }
    }

    /**
     * Checks whether given qualified {@code name == names[0]} matches this literal name in the provided
     * {@code context}. For example, if namespace context contains mapping {@code 'p' -&gt; 'urn:namespace'},
     * {@code names.size() == 1}, {@code names[0]} is equal to {@code {urn:namespace}n:some-name} and {@code this}
     * is equal to {@code p:some-name}, then {@code names} matches {@code this}.
     *
     * @param names a list of qualified names to check; must contain exactly one element
     * @param context mapping between prefixes and namespaces
     * @return {@code true} when {@code names[0]} matches {@code this}, {@code false} otherwise
     */
    @Override
    public boolean matches(List<QualifiedName> names, NamespaceContext context) {
        // More or less than 1 element
        if (names.size() != 1) {
            return false;
        }
        QualifiedName name = names.get(0);

        // This literal name's local name is wildcard or is equal to the qualified name's local name
        if (PathsConstants.WILDCARD.equals(this.name) || this.name.equals(name.localName)) {
            // When literal name's prefix is present
            if (this.prefix.isPresent()) {
                // Prefix is wildcard or is equal to the prefix of qualified name's namespace in the context
                return PathsConstants.WILDCARD.equals(this.prefix.get()) || this.prefix.equals(context.prefixOf(name));
            } else {
                // Prefix is not present, so qualified name must have no namespace
                return !name.namespace.isPresent();
            }
        } else {
            // Local names do no match
            return false;
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

        LiteralName name1 = (LiteralName) o;

        return name.equals(name1.name) && prefix.equals(name1.prefix);
    }

    @Override
    public int hashCode() {
        return name.hashCode() ^ prefix.hashCode();
    }

    /**
     * Returns new literal name with local part of the name only.
     *
     * @param name local name
     * @return new literal name
     */
    static LiteralName localOnly(String name) {
        Preconditions.checkNotNull(name, "Name is null");
        Preconditions.checkArgument(!name.isEmpty(), "Name is empty");

        return new LiteralName(name, Optional.<String>absent());
    }

    /**
     * Returns new literal name with local part and the prefix.
     *
     * @param name local name
     * @param prefix prefix
     * @return new literal name
     */
    static LiteralName withPrefix(String name, String prefix) {
        Preconditions.checkNotNull(name, "Name is null");
        Preconditions.checkNotNull(prefix, "Prefix is null");
        Preconditions.checkArgument(!name.isEmpty(), "Name is empty");
        Preconditions.checkArgument(!prefix.isEmpty(), "Prefix is empty");

        return new LiteralName(name, Optional.of(prefix));
    }

    /**
     * Parses a string and returns literal name written in it. String must be of the following pattern:
     * {@code [prefix ':']local-name}, where square brackets mean optional part, single quotes mean literal character
     * and {@code prefix} and {@code local-name} mean non-empty strings.
     *
     * @param string a string with literal name.
     * @return new literal name
     */
    static LiteralName fromString(String string) {
        Preconditions.checkNotNull(string, "String is null");

        int idx = string.indexOf(':');
        if (idx < 0) {
            return localOnly(string);
        } else {
            return withPrefix(string.substring(idx+1), string.substring(0, idx));
        }
    }
}
