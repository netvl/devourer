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
 * Date: 09.03.13
 * Time: 15:10
 *
 * @author Vladimir Matveev
 */
public final class PatternElements {
    private PatternElements() {
    }

    public static PatternElement fromString(String string) {
        Preconditions.checkNotNull(string, "String is empty");

        if (PathsConstants.GLOBAL_WILDCARD.equals(string)) {
            return globalWildcard();
        } else {
            return LiteralName.fromString(string);
        }
    }

    public static PatternElement literal(String name) {
        return LiteralName.localOnly(name);
    }

    public static PatternElement literal(String name, String prefix) {
        return LiteralName.withPrefix(name, prefix);
    }

    public static PatternElement globalWildcard() {
        return GlobalWildcard.create();
    }
}
