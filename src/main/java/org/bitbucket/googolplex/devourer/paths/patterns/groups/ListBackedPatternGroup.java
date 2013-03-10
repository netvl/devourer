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

package org.bitbucket.googolplex.devourer.paths.patterns.groups;

import com.google.common.base.Optional;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.paths.ExactPath;
import org.bitbucket.googolplex.devourer.paths.patterns.PathPattern;

import java.util.List;

/**
 * Date: 07.03.13
 * Time: 21:56
 *
 * @author Vladimir Matveev
 */
public class ListBackedPatternGroup implements PatternGroup {
    private final List<PathPattern> patterns;

    ListBackedPatternGroup(List<PathPattern> patterns) {
        this.patterns = patterns;
    }

    @Override
    public Optional<PathPattern> lookup(ExactPath path, NamespaceContext namespaceContext) {
        for (PathPattern pattern : patterns) {
            if (pattern.matches(path.parts, namespaceContext)) {
                return Optional.of(pattern);
            }
        }
        return Optional.absent();
    }
}
