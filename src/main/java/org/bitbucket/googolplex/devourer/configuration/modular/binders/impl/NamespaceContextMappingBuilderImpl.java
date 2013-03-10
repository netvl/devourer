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

package org.bitbucket.googolplex.devourer.configuration.modular.binders.impl;

import com.google.common.base.Preconditions;
import org.bitbucket.googolplex.devourer.configuration.modular.binders.NamespaceContextBuilder;
import org.bitbucket.googolplex.devourer.configuration.modular.binders.NamespaceContextMappingBuilder;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;

/**
 * Date: 10.03.13
 * Time: 19:16
 *
 * @author Vladimir Matveev
 */
public class NamespaceContextMappingBuilderImpl implements NamespaceContextMappingBuilder {
    private final NamespaceContextBuilder parent;
    private final NamespaceContext.Builder builder;
    private final String namespace;

    NamespaceContextMappingBuilderImpl(NamespaceContextBuilder parent,
                                       NamespaceContext.Builder builder,
                                       String namespace) {
        this.parent = parent;
        this.builder = builder;
        this.namespace = namespace;
    }

    @Override
    public NamespaceContextBuilder to(String prefix) {
        Preconditions.checkNotNull(prefix, "Prefix is null");
        Preconditions.checkArgument(!prefix.isEmpty(), "Prefix is empty");

        this.builder.add(namespace, prefix);

        return parent;
    }
}
