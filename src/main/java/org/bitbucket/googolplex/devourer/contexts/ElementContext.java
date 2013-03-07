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

/**
 * Element context contains information about the current element. It is provided to actions configured
 * in {@link org.bitbucket.googolplex.devourer.configuration.modular.MappingModule}s or to annotated methods in
 * annotated configuration object.
 *
 * <p>Element context is transient; you should not hold instances of this interface neither in
 * {@link org.bitbucket.googolplex.devourer.stacks.Stacks} not somewhere else.</p>
 */
public interface ElementContext {
    /**
     * @return local name of the currently processed element
     */
    String elementName();

    /**
     * @return namespace prefix of the currently processed element, if present
     */
    Optional<String> elementPrefix();

    /**
     * @return namespace of the currently processed element, if present
     */
    Optional<String> elementNamespace();
}
