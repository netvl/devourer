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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.bitbucket.googolplex.devourer.paths.patterns.PathPattern;

/**
 * Date: 07.03.13
 * Time: 21:51
 *
 * @author Vladimir Matveev
 */
public final class PatternGroups {
    private PatternGroups() {
    }

    public static PatternGroup listBackedGroup(Iterable<? extends PathPattern> patterns) {
        Preconditions.checkNotNull(patterns, "Patterns are null");

        return new ListBackedPatternGroup(ImmutableList.copyOf(patterns));
    }

    public static PatternGroup cachingGroup(Iterable<? extends PathPattern> patterns) {
        Preconditions.checkNotNull(patterns, "Patterns are null");

        return new CachingPatternGroup(listBackedGroup(patterns));
    }

    public static PatternGroupProvider listBackedGroupProvider() {
        return new PatternGroupProvider() {
            @Override
            public PatternGroup create(Iterable<? extends PathPattern> patterns) {
                return listBackedGroup(patterns);
            }
        };
    }

    public static PatternGroupProvider cachingGroupProvider() {
        return new PatternGroupProvider() {
            @Override
            public PatternGroup create(Iterable<? extends PathPattern> patterns) {
                return cachingGroup(patterns);
            }
        };
    }
}
