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

import java.util.Collection;

/**
 * An extension of {@link ElementContext} which adds attribute accessing methods.
 */
public interface AttributesContext extends ElementContext {
    /**
     * @return a collection of all available attribute names
     */
    Collection<PrefixedName> attributeNames();

    /**
     * Returns an attribute with given local name and no namespace.
     *
     * @param name local name of the attribute
     * @return an attribute with {@code name} local name and without a namespace, if present
     */
    Optional<String> attribute(String name);

    /**
     * Returns an attribute with given local name and namespace.
     *
     * @param name local name of the attribute
     * @param prefix namespace prefix of the attribute
     * @return an attribute with {@code name} local name and {@code prefix} namespace prefix, if present
     */
    Optional<String> attribute(String name, String prefix);

    /**
     * Returns an attribute with given prefixed name.
     *
     * @param name full name of the attribute
     * @return an attribute with {@code name} full name, if present
     */
    Optional<String> attribute(PrefixedName name);
}
