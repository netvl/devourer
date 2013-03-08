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

/**
 * Date: 09.03.13
 * Time: 1:42
 *
 * @author Vladimir Matveev
 */
public final class PathPatterns {
    private PathPatterns() {
    }

    /**
     * Returns the most appropriate path pattern which the given string denotes, i.e. if the string contains
     * only simple path elements, then {@link LiteralPathPattern} is returned; if it contains multi-wildcards,
     * {@link ExtendedPathPattern} is returned.
     *
     * @param string a string representing path pattern
     * @return path pattern for the given string
     */
    public final PathPattern parsePattern(String string) {
        return null;
    }
}
