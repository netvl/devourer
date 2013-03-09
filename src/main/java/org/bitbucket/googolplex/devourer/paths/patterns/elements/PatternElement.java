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

import org.bitbucket.googolplex.devourer.paths.patterns.PathPattern;

/**
 * Base class for pattern element. Defines methods which can differentiate between global wildcard and literal name.
 * <p>
 *     TODO: consider removal of this class
 * </p>
 */
public abstract class PatternElement implements PathPattern {
    /**
     * @return {@code true} if this object is literal name, {@code false} otherwise
     */
    public boolean isLiteral() {
        return false;
    }

    /**
     * @return {@code true} if this object is global wildcard, {@code false} otherwise
     */
    public boolean isGlobalWildcard() {
        return false;
    }
}
