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

package org.bitbucket.googolplex.devourer.paths;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAfter;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAt;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionBefore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class with functions related to mappings management.
 */
public final class Mappings {
    private Mappings() {
    }

    /**
     * Combines three multimaps from {@link SimplePath}s to actions into single map
     * from {@link SimplePath}s to {@link PathMapping}s. Used by mapping configuration parsers.
     *
     * @param beforeMappings a collection of before-actions
     * @param atMappings a collection of at-actions
     * @param afterMappings a collection of after-actions
     * @return combined collection of all actions
     */
    public static Map<SimplePath, PathMapping> combineMappings(ListMultimap<SimplePath, ActionBefore> beforeMappings,
                                                         ListMultimap<SimplePath, ActionAt> atMappings,
                                                         ListMultimap<SimplePath, ActionAfter> afterMappings) {
        Map<SimplePath, PathMapping> builder = new HashMap<SimplePath, PathMapping>();
        for (SimplePath path : Iterables.concat(beforeMappings.keySet(), atMappings.keySet(), afterMappings.keySet())) {
            // An order is implicit here
            List<ActionBefore> befores = ImmutableList.copyOf(beforeMappings.get(path));
            List<ActionAt> ats = ImmutableList.copyOf(atMappings.get(path));
            List<ActionAfter> afters = ImmutableList.copyOf(afterMappings.get(path));

            PathMapping mapping = new PathMapping(befores, ats, afters);
            builder.put(path, mapping);
        }
        return ImmutableMap.copyOf(ImmutableMap.copyOf(builder));
    }
}
