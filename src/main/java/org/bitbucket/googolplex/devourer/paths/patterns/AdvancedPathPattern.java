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
import com.google.common.collect.ImmutableList;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedName;
import org.bitbucket.googolplex.devourer.paths.PathsConstants;
import org.bitbucket.googolplex.devourer.paths.patterns.elements.PatternElement;

import java.util.List;

/**
 * Represents a pattern for a path inside an XML document structure, possible with global wildcard patterns.
 */
public class AdvancedPathPattern implements PathPattern {
    private final List<PatternElement> elements;

    private AdvancedPathPattern(List<PatternElement> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        return "/" + PathsConstants.PATH_JOINER.join(elements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(List<QualifiedName> names, NamespaceContext context) {
        return matches(elements, names, context);
    }

    private boolean matches(List<PatternElement> elements, List<QualifiedName> names, NamespaceContext context) {
        int idx = 0;
        // Loop until either pattern or path is exhausted or until global wildcard is found
        for (; idx < elements.size() && idx < names.size(); ++idx) {
            // Exit from the loop if global wildcard was found
            if (elements.get(idx).isGlobalWildcard()) {
                break;
            }
            // Exit with failure if path part does not match pattern element
            if (!elements.get(idx).matches(ImmutableList.of(names.get(idx)), context)) {
                return false;
            }
        }

        // Both pattern and path are exhausted and no conflicts was found - path matches
        if (idx == elements.size() && idx == names.size()) {
            return true;
        }

        // Pattern is at its last element and it is global wildcard; the rest of the path is considered
        // matching, and true is returned
        if (idx == elements.size()-1 && idx <= names.size() && elements.get(idx).isGlobalWildcard()) {
            return true;
        }

        // Pattern is exhausted but path is not - path does not match
        if (idx == elements.size() && idx < names.size()) {
            return false;
        }

        // Pattern is not exhausted but path is; there can be no global wildcard in this branch, so
        // path does not match
        if (idx < elements.size() && idx == names.size()) {
            return false;
        }

        // Neither pattern nor path are exhausted; this can happen only if a global wildcard has been found
        // in the middle of the pattern (it is at idx index). We have to try to match all tail parts against
        // the pattern
        List<PatternElement> subpattern = elements.subList(idx+1, elements.size());
        for (int j = idx; j < names.size(); ++j) {
            // Try for the next possible tail of the path
            List<QualifiedName> subpath = names.subList(j, names.size());
            if (matches(subpattern, subpath, context)) {  // If it matches, then the whole path matches
                return true;
            }
        }

        // None of the subpaths have matched, return failure
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

        AdvancedPathPattern that = (AdvancedPathPattern) o;

        return elements.equals(that.elements);
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }

    /**
     * Creates new {@link AdvancedPathPattern} from a list of {@link PatternElement}s.
     *
     * @param parts a list of pattern elements
     * @return new path pattern
     */
    static AdvancedPathPattern fromList(List<PatternElement> parts) {
        Preconditions.checkNotNull(parts, "Parts are null");

        return new AdvancedPathPattern(parts);
    }
}
