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
import com.google.common.collect.ImmutableMap;

import javax.xml.namespace.QName;
import java.util.Collection;
import java.util.Map;

/**
 * Default implementation of {@link ElementContext} and {@link AttributesContext}. Can be constructed using
 * {@link Builder}.
 */
public class DefaultAttributesContext implements AttributesContext {
    private final PrefixedName name;
    private final Map<PrefixedName, String> attributes;

    private DefaultAttributesContext(PrefixedName name, Map<PrefixedName, String> attributes) {
        Preconditions.checkNotNull(name, "Name is null");
        Preconditions.checkNotNull(attributes, "Attributes are null");

        this.name = name;
        this.attributes = attributes;
    }

    @Override
    public Collection<PrefixedName> attributeNames() {
        return attributes.keySet();
    }

    @Override
    public Optional<String> attribute(String name) {
        return Optional.fromNullable(attributes.get(PrefixedName.simple(name)));
    }

    @Override
    public Optional<String> attribute(String name, String prefix) {
        return Optional.fromNullable(attributes.get(PrefixedName.withPrefix(prefix, name)));
    }

    @Override
    public Optional<String> attribute(PrefixedName name) {
        return Optional.fromNullable(attributes.get(name));
    }

    @Override
    public String elementName() {
        return name.name;
    }

    @Override
    public Optional<String> elementPrefix() {
        return name.prefix;
    }

    public static class Builder {
        private final ImmutableMap.Builder<PrefixedName, String> attributesBuilder = ImmutableMap.builder();
        private PrefixedName name;

        public Builder setName(QName name) {
            Preconditions.checkNotNull(name, "Name is null");

            this.name = PrefixedName.fromQName(name);
            return this;
        }

        public Builder setName(String name) {
            Preconditions.checkNotNull(name, "Name is null");

            this.name = PrefixedName.simple(name);
            return this;
        }

        public Builder setName(String name, String namespace) {
            Preconditions.checkNotNull(name, "Name is null");
            Preconditions.checkNotNull(namespace, "Namespace is null");

            this.name = PrefixedName.withPrefix(namespace, name);
            return this;
        }

        public Builder addAttribute(String name, String value) {
            Preconditions.checkNotNull(name, "Attribute name is null");
            Preconditions.checkNotNull(value, "Attribute value is null");

            this.attributesBuilder.put(PrefixedName.simple(name), value);
            return this;
        }

        public Builder addAttribute(String name, String namespace, String value) {
            Preconditions.checkNotNull(name, "Attribute name is null");
            Preconditions.checkNotNull(namespace, "Attribute namespace is null");
            Preconditions.checkNotNull(value, "Attribute value is null");

            this.attributesBuilder.put(PrefixedName.withPrefix(namespace, name), value);
            return this;
        }

        public Builder addAttribute(QName name, String value) {
            Preconditions.checkNotNull(name, "Attribute name is null");
            Preconditions.checkNotNull(value, "Attribute value is null");

            this.attributesBuilder.put(PrefixedName.fromQName(name), value);
            return this;
        }

        public DefaultAttributesContext build() {
            return new DefaultAttributesContext(name, attributesBuilder.build());
        }
    }
}
