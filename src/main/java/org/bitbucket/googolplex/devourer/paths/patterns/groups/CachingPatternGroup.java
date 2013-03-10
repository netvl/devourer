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
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.paths.ExactPath;
import org.bitbucket.googolplex.devourer.paths.patterns.PathPattern;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Date: 10.03.13
 * Time: 19:25
 *
 * @author Vladimir Matveev
 */
public class CachingPatternGroup implements PatternGroup {
    private final PatternGroup inner;
    private final Cache<ExactPath, Optional<PathPattern>> cache =
        CacheBuilder.newBuilder()
            .maximumSize(500)
            .build();

    CachingPatternGroup(PatternGroup inner) {
        this.inner = inner;
    }

    @Override
    public Optional<PathPattern> lookup(final ExactPath path, final NamespaceContext namespaceContext) {
        try {
            return cache.get(path, new Callable<Optional<PathPattern>>() {
                @Override
                public Optional<PathPattern> call() {
                    return inner.lookup(path, namespaceContext);
                }
            });
        } catch (ExecutionException e) {
            // This is impossible
            throw new RuntimeException(e);
        }
    }
}
