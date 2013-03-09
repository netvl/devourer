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

import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedName;

import java.util.List;

/**
 * A pattern against which a path inside an XML document can be matched. The path in this interface is
 * represented by a list of {@link QualifiedName}s, but such paths are usually handled through
 * {@link org.bitbucket.googolplex.devourer.paths.ExactPath} class.
 */
public interface PathPattern {
    /**
     * Checks whether given path inside an XML document (represented by a list of {@link QualifiedName}s)
     * matches this pattern. {@link NamespaceContext} instance is used to resolve prefixes specified in the
     * pattern against namespaces in the path.
     *
     * @param names a list of qualified names which designates a path inside an XML document
     * @param context namespace context used for prefixes resolution
     * @return {@code true} if given path matches this pattern, {@code false} otherwise
     */
    boolean matches(List<QualifiedName> names, NamespaceContext context);
}
