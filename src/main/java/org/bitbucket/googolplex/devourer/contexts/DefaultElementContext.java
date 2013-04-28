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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedName;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedNames;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default implementation of {@link ElementContext}. Can be constructed using {@link Builder}.
 */
public class DefaultElementContext implements ElementContext {
    private final QualifiedName name;
    private final javax.xml.namespace.NamespaceContext realNamespaceContext;
    private final Map<QualifiedName, String> attributes;
    private final NamespaceContext customNamespaceContext;

    private DefaultElementContext(QualifiedName name, 
                                  javax.xml.namespace.NamespaceContext realNamespaceContext,
                                  NamespaceContext customNamespaceContext,
                                  Map<QualifiedName, String> attributes) {
        checkNotNull(name, "Name is null");
        checkNotNull(realNamespaceContext, "Real namespace context is null");
        checkNotNull(customNamespaceContext, "Custom namespace context is null");
        checkNotNull(attributes, "Attributes are null");

        this.name = name;
        this.realNamespaceContext = realNamespaceContext;
        this.customNamespaceContext = customNamespaceContext;
        this.attributes = ImmutableMap.copyOf(attributes);
    }

    @Override
    public QualifiedName elementName() {
        return name;
    }

    @Override
    public javax.xml.namespace.NamespaceContext realNamespaceContext() {
        return realNamespaceContext;
    }

    @Override
    public NamespaceContext customNamespaceContext() {
        return customNamespaceContext;
    }


    @Override
    public Collection<QualifiedName> attributeNames() {
        return attributes.keySet();
    }

    @Override
    public Optional<String> attribute(String localName) {
        checkNotNull(localName, "Local name is null");

        return Optional.fromNullable(attributes.get(QualifiedNames.localOnly(localName)));
    }

    @Override
    public Optional<String> attribute(String localName, String namespace) {
        checkNotNull(localName, "Local name is null");
        checkNotNull(namespace, "Namespace is null");

        return Optional.fromNullable(attributes.get(QualifiedNames.withNamespace(localName, namespace)));
    }

    @Override
    public Optional<String> attributeWithPrefix(String localName, String prefix) {
        checkNotNull(localName, "Local name is null");
        checkNotNull(prefix, "Prefix is null");

        Optional<String> namespace = customNamespaceContext.namespace(prefix);
        if (namespace.isPresent()) {
            return Optional.fromNullable(attributes.get(QualifiedNames.withNamespace(localName, namespace.get())));
        } else {
            return Optional.absent();
        }
    }

    @Override
    public Optional<String> attribute(QualifiedName name) {
        checkNotNull(name, "Name is null");

        return Optional.fromNullable(attributes.get(name));
    }

    public static class Builder {
        private final HashMap<QualifiedName, String> attributes = Maps.newHashMap();
        private javax.xml.namespace.NamespaceContext realNamespaceContext;
        private NamespaceContext customNamespaceContext;
        private QualifiedName name;

        public Builder setRealNamespaceContext(javax.xml.namespace.NamespaceContext realNamespaceContext) {
            checkNotNull(realNamespaceContext, "Real namespace context is null");

            this.realNamespaceContext = realNamespaceContext;
            return this;
        }

        public Builder setCustomNamespaceContext(NamespaceContext customNamespaceContext) {
            checkNotNull(customNamespaceContext, "Custom namespace context is null");

            this.customNamespaceContext = customNamespaceContext;
            return this;
        }

        public Builder setName(QualifiedName name) {
            checkNotNull(name, "Name is null");

            this.name = name;
            return this;
        }

        public Builder setName(String name) {
            checkNotNull(name, "Name is null");

            this.name = QualifiedNames.localOnly(name);
            return this;
        }

        public Builder setName(String name, String namespace) {
            checkNotNull(name, "Name is null");
            checkNotNull(namespace, "Namespace is null");

            this.name = QualifiedNames.withNamespace(name, namespace);
            return this;
        }

        public Builder setName(String name, String namespace, String prefix) {
            checkNotNull(name, "Name is null");
            checkNotNull(namespace, "Namespace is null");
            checkNotNull(prefix, "Prefix is null");

            this.name = QualifiedNames.full(name, namespace, prefix);
            return this;
        }

        public Builder addAttribute(String name, String value) {
            checkNotNull(name, "Attribute name is null");
            checkNotNull(value, "Attribute value is null");

            this.attributes.put(QualifiedNames.localOnly(name), value);
            return this;
        }

        public Builder addAttribute(String name, String namespace, String value) {
            checkNotNull(name, "Attribute name is null");
            checkNotNull(namespace, "Attribute namespace is null");
            checkNotNull(value, "Attribute value is null");

            this.attributes.put(QualifiedNames.withNamespace(name, namespace), value);
            return this;
        }

        public Builder addAttribute(String name, String namespace, String prefix, String value) {
            checkNotNull(name, "Attribute name is null");
            checkNotNull(namespace, "Attribute namespace is null");
            checkNotNull(prefix, "Attribute prefix is null");
            checkNotNull(value, "Attribute value is null");

            this.attributes.put(QualifiedNames.full(name, namespace, prefix), value);
            return this;
        }

        public Builder addAttribute(QualifiedName name, String value) {
            checkNotNull(name, "Attribute name is null");
            checkNotNull(value, "Attribute value is null");

            this.attributes.put(name, value);
            return this;
        }

        public DefaultElementContext build() {
            return new DefaultElementContext(name, realNamespaceContext, customNamespaceContext, attributes);
        }
    }
}
