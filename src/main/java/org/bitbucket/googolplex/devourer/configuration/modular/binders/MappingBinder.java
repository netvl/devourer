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

package org.bitbucket.googolplex.devourer.configuration.modular.binders;

import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.paths.mappings.PathMapping;

/**
 * See DSL usage examples in {@link org.bitbucket.googolplex.devourer.configuration.modular.AbstractMappingModule}
 * documentation.
 */
public interface MappingBinder {
    /**
     * See DSL usage examples in {@link org.bitbucket.googolplex.devourer.configuration.modular.AbstractMappingModule}
     * documentation.
     */
    ActionBindingBuilder on(String route);

    /**
     * See DSL usage examples in {@link org.bitbucket.googolplex.devourer.configuration.modular.AbstractMappingModule}
     * documentation.
     */
    NamespaceContextBuilder namespaceContext();

    /**
     * A method for internal usage. Returns accumulated path mapping.
     */
    PathMapping getMapping();

    /**
     * A method for internal usage. Returns accumulated namespace context.
     */
    NamespaceContext getNamespaceContext();
}
