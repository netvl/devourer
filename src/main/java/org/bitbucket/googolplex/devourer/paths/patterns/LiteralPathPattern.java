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
import org.bitbucket.googolplex.devourer.paths.patterns.parts.LiteralName;

import java.util.Iterator;
import java.util.List;

/**
 * Path pattern consisting only of literal names. This pattern's matching speed is better than the one of
 * {@link ExtendedPathPattern}.
 */
public class LiteralPathPattern implements PathPattern {
    private final List<LiteralName> parts;

    private LiteralPathPattern(List<LiteralName> parts) {
        this.parts = parts;
    }

    @Override
    public String toString() {
        return "/" + PathsConstants.PATH_JOINER.join(parts);
    }

    @Override
    public boolean matches(List<QualifiedName> names, NamespaceContext context) {
        // Must be of the same length
        if (names.size() != parts.size()) {
            return false;
        }

        Iterator<QualifiedName> ni = names.iterator();
        Iterator<LiteralName> li = parts.iterator();
        while (ni.hasNext() && li.hasNext()) {
            QualifiedName name = ni.next();
            LiteralName part = li.next();

            if (!part.matches(ImmutableList.of(name), context)) {
                return false;
            }
        }

        return true;
    }

    public static LiteralPathPattern fromList(List<LiteralName> parts) {
        Preconditions.checkNotNull(parts, "Parts are null");

        return new LiteralPathPattern(parts);
    }
}
