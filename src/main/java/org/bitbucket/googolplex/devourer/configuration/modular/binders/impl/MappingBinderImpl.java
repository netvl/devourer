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

import org.bitbucket.googolplex.devourer.configuration.modular.binders.ActionBindingBuilder;
import org.bitbucket.googolplex.devourer.configuration.modular.binders.MappingBinder;
import org.bitbucket.googolplex.devourer.configuration.modular.binders.NamespaceContextBuilder;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.paths.mappings.MappingBuilder;
import org.bitbucket.googolplex.devourer.paths.mappings.PathMapping;

public class MappingBinderImpl implements MappingBinder {
    protected final MappingBuilder mappingBuilder = MappingBuilder.create();
    protected final NamespaceContext.Builder namespaceContext = NamespaceContext.builder();

    @Override
    public ActionBindingBuilder on(String route) {
        return new ActionBindingBinderImpl(mappingBuilder, route);
    }

    @Override
    public NamespaceContextBuilder namespaceContext() {
        return new NamespaceContextBuilderImpl(namespaceContext);
    }

    public PathMapping getMapping() {
        return mappingBuilder.build();
    }

    @Override
    public NamespaceContext getNamespaceContext() {
        return namespaceContext.build();
    }

}
