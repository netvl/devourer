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
import org.bitbucket.googolplex.devourer.paths.PathsConstants;
import org.bitbucket.googolplex.devourer.paths.patterns.elements.PatternElement;
import org.bitbucket.googolplex.devourer.paths.patterns.elements.PatternElements;

/**
 * Contains factory methods for creating instances of {@link PathPattern} interface.
 */
public final class PathPatterns {
    private PathPatterns() {
    }

    /**
     * Returns an implementation of {@link PathPattern} interface which will match
     * {@link org.bitbucket.googolplex.devourer.paths.ExactPath}s against a pattern defined by the given string.
     * <p>The string must conform to the following pattern: <code>'/' | ('/' ([prefix ':']local-name | '**'))+</code>,
     * that is, the following strings are possible:
     * <ul>
     *     <li><code>/</code></li>
     *     <li><code>/**</code></li>
     *     <li><code>/a/b/c</code></li>
     *     <li><code>/a/p:b/*:c</code></li>
     *     <li><code>/n:a/*:*&#47;*</code></li>
     *     <li><code>/a/**&#47;b</code></li>
     * </ul>
     * The parser is liberal, however, it will ignore several sequential slashes and it also allows
     * leaving out the leading or putting in the trailing slash. All leading and trailing whitespace characters
     * in each element will be ignored.</p>
     *
     * @param string a string representing path pattern
     * @return path pattern for the given string
     */
    public static PathPattern fromString(String string) {
        Preconditions.checkNotNull(string, "String is null");

        ImmutableList.Builder<PatternElement> result = ImmutableList.builder();

        Iterable<String> parts = PathsConstants.PATH_SPLITTER.split(string);
        for (String part : parts) {
            PatternElement element = PatternElements.fromString(part);
            result.add(element);
        }

        return AdvancedPathPattern.fromList(result.build());
    }
}
