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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedName;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedNames;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link ElementContext} and {@link AttributesContext}. Can be constructed using
 * {@link Builder}.
 */
public class DefaultAttributesContext implements AttributesContext {
    private final QualifiedName name;
    private final NamespaceContext namespaceContext;
    private final Map<QualifiedName, String> attributes;

    private DefaultAttributesContext(QualifiedName name, NamespaceContext namespaceContext,
                                     Map<QualifiedName, String> attributes) {
        Preconditions.checkNotNull(name, "Name is null");
        Preconditions.checkNotNull(namespaceContext, "Namespace context is null");
        Preconditions.checkNotNull(attributes, "Attributes are null");

        this.name = name;
        this.namespaceContext = namespaceContext;
        this.attributes = ImmutableMap.copyOf(attributes);
    }

    @Override
    public QualifiedName elementName() {
        return name;
    }

    @Override
    public NamespaceContext namespaceContext() {
        return namespaceContext;
    }

    @Override
    public Collection<QualifiedName> attributeNames() {
        return attributes.keySet();
    }

    @Override
    public Optional<String> attribute(String localName) {
        Preconditions.checkNotNull(localName, "Local name is null");

        return Optional.fromNullable(attributes.get(QualifiedNames.localOnly(localName)));
    }

    @Override
    public Optional<String> attribute(String localName, String namespace) {
        Preconditions.checkNotNull(localName, "Local name is null");
        Preconditions.checkNotNull(namespace, "Namespace is null");

        return Optional.fromNullable(attributes.get(QualifiedNames.withNamespace(localName, namespace)));
    }

    @Override
    public Optional<String> attributeWithPrefix(String localName, String prefix) {
        Preconditions.checkNotNull(localName, "Local name is null");
        Preconditions.checkNotNull(prefix, "Prefix is null");

        String namespace = namespaceContext.getNamespaceURI(prefix);
        if (XMLConstants.NULL_NS_URI.equals(namespace)) {
            return Optional.absent();
        } else {
            return Optional.fromNullable(attributes.get(QualifiedNames.withNamespace(localName, namespace)));
        }
    }

    @Override
    public Optional<String> attribute(QualifiedName name) {
        Preconditions.checkNotNull(name, "Name is null");

        return Optional.fromNullable(attributes.get(name));
    }

    public static class Builder {
        private final HashMap<QualifiedName, String> attributes = Maps.newHashMap();
        private NamespaceContext namespaceContext;
        private QualifiedName name;

        public Builder setNamespaceContext(NamespaceContext namespaceContext) {
            Preconditions.checkNotNull(namespaceContext, "Namespace context is null");

            this.namespaceContext = namespaceContext;
            return this;
        }

        public Builder setName(QualifiedName name) {
            Preconditions.checkNotNull(name, "Name is null");

            this.name = name;
            return this;
        }

        public Builder setName(String name) {
            Preconditions.checkNotNull(name, "Name is null");

            this.name = QualifiedNames.localOnly(name);
            return this;
        }

        public Builder setName(String name, String namespace) {
            Preconditions.checkNotNull(name, "Name is null");
            Preconditions.checkNotNull(namespace, "Namespace is null");

            this.name = QualifiedNames.withNamespace(name, namespace);
            return this;
        }

        public Builder setName(String name, String namespace, String prefix) {
            Preconditions.checkNotNull(name, "Name is null");
            Preconditions.checkNotNull(namespace, "Namespace is null");
            Preconditions.checkNotNull(prefix, "Prefix is null");

            this.name = QualifiedNames.full(name, namespace, prefix);
            return this;
        }

        public Builder addAttribute(String name, String value) {
            Preconditions.checkNotNull(name, "Attribute name is null");
            Preconditions.checkNotNull(value, "Attribute value is null");

            this.attributes.put(QualifiedNames.localOnly(name), value);
            return this;
        }

        public Builder addAttribute(String name, String namespace, String value) {
            Preconditions.checkNotNull(name, "Attribute name is null");
            Preconditions.checkNotNull(namespace, "Attribute namespace is null");
            Preconditions.checkNotNull(value, "Attribute value is null");

            this.attributes.put(QualifiedNames.withNamespace(name, namespace), value);
            return this;
        }

        public Builder addAttribute(String name, String namespace, String prefix, String value) {
            Preconditions.checkNotNull(name, "Attribute name is null");
            Preconditions.checkNotNull(namespace, "Attribute namespace is null");
            Preconditions.checkNotNull(prefix, "Attribute prefix is null");
            Preconditions.checkNotNull(value, "Attribute value is null");

            this.attributes.put(QualifiedNames.full(name, namespace, prefix), value);
            return this;
        }

        public Builder addAttribute(QualifiedName name, String value) {
            Preconditions.checkNotNull(name, "Attribute name is null");
            Preconditions.checkNotNull(value, "Attribute value is null");

            this.attributes.put(name, value);
            return this;
        }

        public DefaultAttributesContext build() {
            return new DefaultAttributesContext(name, namespaceContext, attributes);
        }
    }
}
