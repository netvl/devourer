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
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;

import java.util.Map;
import java.util.Set;

/**
 * Represents bidirectional map from namespaces to prefixes. This class is intended to be used solely for
 * definition of mapping between namespaces and prefixes, so it does not allow empty prefixes or namespaces.
 * Do not mistake this class with {@link javax.xml.namespace.NamespaceContext}, it serves different purpose.
 * This class also does not support mapping multiple prefixes to single namespace.
 */
public class NamespaceContext {
    private final BiMap<String, String> namespaceMap;

    private NamespaceContext(BiMap<String, String> namespaceMap) {
        for (BiMap.Entry<String, String> entry : namespaceMap.entrySet()) {
            Preconditions.checkArgument(!entry.getKey().isEmpty(), "Map contains empty namespace");
            Preconditions.checkArgument(!entry.getValue().isEmpty(), "Map contains empty prefix");
        }
        this.namespaceMap = ImmutableBiMap.copyOf(namespaceMap);
    }

    /**
     * Creates new qualified name using local name part and prefix. The prefix must define a namespace in this
     * {@link NamespaceContext}. If no such namespace is present in the context, absent value is returned.
     *
     * @param name local name
     * @param prefix namespace prefix
     * @return fully qualified name, if prefix defines a namespace in this context, absent value otherwise
     */
    public Optional<QualifiedName> qualifiedName(String name, String prefix) {
        Preconditions.checkNotNull(name, "Name is null");
        Preconditions.checkArgument(!name.isEmpty(), "Name is empty");
        Preconditions.checkNotNull(prefix, "Prefix is null");

        String namespace = namespaceMap.inverse().get(prefix);
        if (namespace == null) {
            return Optional.absent();
        } else {
            return Optional.of(QualifiedNames.full(name, namespace, prefix));
        }
    }

    /**
     * Returns namespace mapped to the given prefix in this context, if such mapping is present.
     *
     * @param prefix namespace prefix
     * @return namespace to which {@code prefix} is mapped, if present
     */
    public Optional<String> namespace(String prefix) {
        Preconditions.checkNotNull(prefix, "Prefix is null");

        return Optional.fromNullable(namespaceMap.inverse().get(prefix));
    }

    /**
     * Returns prefix which is mapped to the given namespace, if such mapping is present.
     *
     * @param namespace namespace
     * @return prefix mapped to {@code namespace}, if present
     */
    public Optional<String> prefix(String namespace) {
        Preconditions.checkNotNull(namespace, "Namespace is null");

        return Optional.fromNullable(namespaceMap.get(namespace));
    }

    /**
     * Returnd prefix which is mapped to namespace of the given element, if such mapping is present.
     *
     * @param name qualified name
     * @return prefix mapped to {@code name.namespace}, if present
     */
    public Optional<String> prefixOf(QualifiedName name) {
        if (name.namespace.isPresent()) {
            return prefix(name.namespace.get());
        } else {
            return Optional.absent();
        }
    }

    /**
     * @return a set of all namespaces in this context
     */
    public Set<String> namespaces() {
        return namespaceMap.keySet();
    }

    /**
     * @return a set of all prefixes in this context
     */
    public Set<String> prefixes() {
        return namespaceMap.values();
    }

    /**
     * Creates new namespace context from pairwise mapping in the arguments. Example:
     * <pre>
     *     NamespaceContext context = NamespaceContext.fromItems(
     *         "http://example.com/namespace-1", "n1",
     *         "http://example.com/namespace-2", "n2"
     *     );
     * </pre>
     * No null or empty items are allowed.
     *
     * @param items mapping from namespaces to prefixes, pairwise
     * @return new namespace context
     */
    public static NamespaceContext fromItems(String... items) {
        Preconditions.checkNotNull(items, "Items are null");
        Preconditions.checkArgument((items.length & 1) == 1, "Items length is odd");

        BiMap<String, String> namespaceMap = HashBiMap.create();
        for (int i = 0; i + 1 < items.length; i += 2) {
            namespaceMap.put(items[i], items[i+1]);
        }

        return new NamespaceContext(ImmutableBiMap.copyOf(namespaceMap));
    }

    /**
     * Creates new namespace context from a map from namespaces to prefixes. No null or empty items are allowed.
     *
     * @param namespaceMap a map from namespaces to prefixes
     * @return new namespace context
     */
    public static NamespaceContext fromMap(Map<String, String> namespaceMap) {
        Preconditions.checkNotNull(namespaceMap, "Namespace map is null");

        return new NamespaceContext(ImmutableBiMap.copyOf(namespaceMap));
    }
}
