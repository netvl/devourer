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

package org.bitbucket.googolplex.devourer.paths.groups;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.bitbucket.googolplex.devourer.paths.Path;

import java.util.Arrays;

/**
 * Date: 07.03.13
 * Time: 21:51
 *
 * @author Vladimir Matveev
 */
public final class PathGroups {
    private PathGroups() {
    }

    public static PathGroup aggregateSequentially(PathGroup... groups) {
        Preconditions.checkNotNull(groups, "Groups are null");

        return aggregateSequentially(Arrays.asList(groups));
    }

    public static PathGroup aggregateSequentially(Iterable<? extends PathGroup> groups) {
        Preconditions.checkNotNull(groups, "Groups are null");

        return new AggregatingPathGroup(groups);
    }

    /**
     * An implementation of {@link PathGroup} which combines several {@link PathGroup}s together. Lookup is sequential,
     * that is, path groups will be queried in the specified order. This allows tweaking the performance by moving
     * the easiest to query groups closer to the head of the sequence.
     */
    private static class AggregatingPathGroup implements PathGroup {
        private final Iterable<? extends PathGroup> groups;

        private AggregatingPathGroup(Iterable<? extends PathGroup> groups) {
            this.groups = groups;
        }

        @Override
        public Optional<Path> lookup(Path path) {
            for (PathGroup group : groups) {
                Optional<Path> result = group.lookup(path);
                if (result.isPresent()) {
                    return result;
                }
            }
            return Optional.absent();
        }
    }
}
