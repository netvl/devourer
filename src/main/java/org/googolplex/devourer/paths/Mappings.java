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

package org.googolplex.devourer.paths;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import org.googolplex.devourer.configuration.reactions.ReactionAfter;
import org.googolplex.devourer.configuration.reactions.ReactionAt;
import org.googolplex.devourer.configuration.reactions.ReactionBefore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 19.02.13
 * Time: 19:14
 *
 * @author Vladimir Matveev
 */
public final class Mappings {
    private Mappings() {
    }

    public static Map<Path, PathMapping> combineMappings(ListMultimap<Path, ReactionBefore> beforeMappings,
                                                         ListMultimap<Path, ReactionAt> atMappings,
                                                         ListMultimap<Path, ReactionAfter> afterMappings) {
        Map<Path, PathMapping> builder = new HashMap<Path, PathMapping>();
        for (Path path : Iterables.concat(beforeMappings.keySet(), atMappings.keySet(), afterMappings.keySet())) {
            // An order is implicit here
            List<ReactionBefore> befores = ImmutableList.copyOf(beforeMappings.get(path));
            List<ReactionAt> ats = ImmutableList.copyOf(atMappings.get(path));
            List<ReactionAfter> afters = ImmutableList.copyOf(afterMappings.get(path));

            PathMapping mapping = new PathMapping(befores, ats, afters);
            builder.put(path, mapping);
        }
        return ImmutableMap.copyOf(ImmutableMap.copyOf(builder));
    }
}
