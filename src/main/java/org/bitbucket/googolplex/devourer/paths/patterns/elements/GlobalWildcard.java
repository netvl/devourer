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

package org.bitbucket.googolplex.devourer.paths.patterns.elements;

import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedName;
import org.bitbucket.googolplex.devourer.paths.PathsConstants;

import java.util.List;

/**
 * Date: 09.03.13
 * Time: 14:35
 *
 * @author Vladimir Matveev
 */
class GlobalWildcard extends PatternElement {
    private GlobalWildcard() {
    }

    @Override
    public boolean matches(List<QualifiedName> names, NamespaceContext context) {
        return true;  // Global wildcard matches everything
    }

    @Override
    public boolean isGlobalWildcard() {
        return true;
    }

    @Override
    public String toString() {
        return PathsConstants.GLOBAL_WILDCARD;
    }

    private static GlobalWildcard INSTANCE = new GlobalWildcard();

    static GlobalWildcard create() {
        return INSTANCE;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return obj == this;
    }
}
