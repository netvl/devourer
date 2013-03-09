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

import com.google.common.base.Preconditions;
import org.bitbucket.googolplex.devourer.paths.PathsConstants;

/**
 * Contains factory methods for creating different pattern elements.
 */
public final class PatternElements {
    private PatternElements() {
    }

    /**
     * Parses given string and returns a pattern element represented by it. The string must match the following
     * pattern: <code>('**' | [prefix ':']local-name)</code>.
     *
     * @param string pattern
     * @return new appropriate pattern element
     */
    public static PatternElement fromString(String string) {
        Preconditions.checkNotNull(string, "String is empty");

        if (PathsConstants.GLOBAL_WILDCARD.equals(string)) {
            return globalWildcard();
        } else {
            return LiteralName.fromString(string);
        }
    }

    /**
     * Returns literal pattern element consisting of local name only. Name can be equal to {@code *}, which
     * implies single wildcard.
     *
     * @param name local name or {@code *}
     * @return literal pattern element
     */
    public static PatternElement literal(String name) {
        return LiteralName.localOnly(name);
    }

    /**
     * Returns literal pattern element consisting of local name and prefix. Name and prefix can be equal to {@code *},
     * which implies single wildcard, either in local name or prefix or both.
     *
     * @param name local name or {@code *}
     * @param prefix prefix or {@code *}
     * @return literal pattern element
     */
    public static PatternElement literal(String name, String prefix) {
        return LiteralName.withPrefix(name, prefix);
    }

    /**
     * Returns global wildcard pattern element. Global wildcard matches everything at any number of levels.
     *
     * @return global wildcard pattern element
     */
    public static PatternElement globalWildcard() {
        return GlobalWildcard.get();
    }
}
