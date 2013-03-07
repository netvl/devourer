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

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import java.util.Collection;
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
        this.attributes = attributes;
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
        return Optional.fromNullable(attributes.get(QualifiedNames.localOnly(localName)));
    }

    @Override
    public Optional<String> attribute(String localName, String namespace) {
        return Optional.fromNullable(attributes.get(QualifiedNames.withNamespace(localName, namespace)));
    }

    @Override
    public Optional<String> attributeWithPrefix(String localName, String prefix) {
        String namespace = namespaceContext.getNamespaceURI(prefix);
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Optional<String> attribute(QualifiedName name) {
        return Optional.fromNullable(attributes.get(name));
    }

    @Override
    public String elementName() {
        return name.localName;
    }

    @Override
    public Optional<String> elementPrefix() {
        return name.prefix;
    }

    @Override
    public Optional<String> elementNamespace() {
        return name.namespace;
    }

    public static class Builder {
        private final ImmutableMap.Builder<QName, String> attributesBuilder = ImmutableMap.builder();
        private NamespaceContext namespaceContext;
        private QName name;

        public Builder setNamespaceContext(NamespaceContext namespaceContext) {
            Preconditions.checkNotNull(namespaceContext, "Namespace context is null");

            this.namespaceContext = namespaceContext;
            return this;
        }

        public Builder setName(QName name) {
            Preconditions.checkNotNull(name, "Name is null");

            this.name = name;
            return this;
        }

        public Builder setName(String name) {
            Preconditions.checkNotNull(name, "Name is null");

            this.name = new QName(name);
            return this;
        }

        public Builder setName(String name, String namespace) {
            Preconditions.checkNotNull(name, "Name is null");
            Preconditions.checkNotNull(namespace, "Namespace is null");

            this.name = new QName(namespace, name);
            return this;
        }

        public Builder setName(String name, String namespace, String prefix) {
            Preconditions.checkNotNull(name, "Name is null");
            Preconditions.checkNotNull(namespace, "Namespace is null");
            Preconditions.checkNotNull(prefix, "Prefix is null");

            this.name = new QName(namespace, name, prefix);
            return this;
        }

        public Builder addAttribute(String name, String value) {
            Preconditions.checkNotNull(name, "Attribute name is null");
            Preconditions.checkNotNull(value, "Attribute value is null");

            this.attributesBuilder.put(new QName(name), value);
            return this;
        }

        public Builder addAttribute(String name, String namespace, String value) {
            Preconditions.checkNotNull(name, "Attribute name is null");
            Preconditions.checkNotNull(namespace, "Attribute namespace is null");
            Preconditions.checkNotNull(value, "Attribute value is null");

            this.attributesBuilder.put(new QName(namespace, name), value);
            return this;
        }

        public Builder addAttribute(String name, String namespace, String prefix, String value) {
            Preconditions.checkNotNull(name, "Attribute name is null");
            Preconditions.checkNotNull(namespace, "Attribute namespace is null");
            Preconditions.checkNotNull(prefix, "Attribute prefix is null");
            Preconditions.checkNotNull(value, "Attribute value is null");

            this.attributesBuilder.put(new QName(namespace, name, prefix), value);
            return this;
        }

        public Builder addAttribute(QName name, String value) {
            Preconditions.checkNotNull(name, "Attribute name is null");
            Preconditions.checkNotNull(value, "Attribute value is null");

            this.attributesBuilder.put(name, value);
            return this;
        }

        public DefaultAttributesContext build() {
            return null;
        }
    }
}
