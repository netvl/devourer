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

package org.bitbucket.googolplex.devourer.paths.patterns;

import com.google.common.base.Preconditions;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedName;
import org.bitbucket.googolplex.devourer.paths.PathsConstants;
import org.bitbucket.googolplex.devourer.paths.patterns.elements.PatternElement;

import java.util.List;

/**
 * Represents some path inside tree structure, including patterns.
 */
class ExtendedPathPattern implements PathPattern {
    private final List<PatternElement> elements;

    private ExtendedPathPattern(List<PatternElement> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        return "/" + PathsConstants.PATH_JOINER.join(elements);
    }

    @Override
    public boolean matches(List<QualifiedName> names, NamespaceContext context) {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExtendedPathPattern that = (ExtendedPathPattern) o;

        return elements.equals(that.elements);
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }

    static ExtendedPathPattern fromList(List<PatternElement> parts) {
        Preconditions.checkNotNull(parts, "Parts are null");

        return new ExtendedPathPattern(parts);
    }
}