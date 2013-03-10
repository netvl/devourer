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

package org.bitbucket.googolplex.devourer.paths.mappings;

import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAfter;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAt;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionBefore;
import org.bitbucket.googolplex.devourer.paths.patterns.PathPattern;
import org.bitbucket.googolplex.devourer.paths.patterns.groups.PatternGroup;
import org.bitbucket.googolplex.devourer.paths.patterns.groups.PatternGroupProvider;
import org.bitbucket.googolplex.devourer.paths.patterns.groups.PatternGroups;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A builder which constructs {@link PathMapping}s. It stores insertion order of all items added to it, which is
 * important for {@link PatternGroup} implementations which depend on pattern order.
 */
public class MappingBuilder {
    private ListMultimap<PathPattern, ActionBefore> beforeMappings = ArrayListMultimap.create();
    private ListMultimap<PathPattern, ActionAt> atMappings = ArrayListMultimap.create();
    private ListMultimap<PathPattern, ActionAfter> afterMappings = ArrayListMultimap.create();
    private PatternGroupProvider patternGroupProvider = PatternGroups.cachingGroupProvider();
    private LinkedHashSet<PathPattern> patterns = new LinkedHashSet<PathPattern>();

    protected MappingBuilder() {
    }

    public static MappingBuilder create() {
        return new MappingBuilder();
    }

    public MappingBuilder withPatternGroupProvider(PatternGroupProvider provider) {
        Preconditions.checkNotNull(provider, "Provider is null");

        this.patternGroupProvider = provider;
        return this;
    }

    private <T> MappingBuilder add(ListMultimap<PathPattern, T> map, PathPattern pattern, T action) {
        Preconditions.checkNotNull(pattern, "Pattern is null");
        Preconditions.checkNotNull(action, "Action is null");

        this.patterns.add(pattern);
        map.put(pattern, action);
        return this;
    }

    public MappingBuilder add(PathPattern pattern, ActionBefore actionBefore) {
        return add(beforeMappings, pattern, actionBefore);
    }

    public MappingBuilder add(PathPattern pattern, ActionAt actionAt) {
        return add(atMappings, pattern, actionAt);
    }

    public MappingBuilder add(PathPattern pattern, ActionAfter actionAfter) {
        return add(afterMappings, pattern, actionAfter);
    }

    private Map<PathPattern, ActionBundle> buildMap() {
        Set<PathPattern> allKeys = Sets.newHashSet();
        allKeys.addAll(beforeMappings.keys());
        allKeys.addAll(atMappings.keys());
        allKeys.addAll(afterMappings.keys());

        Map<PathPattern, ActionBundle> result = Maps.newHashMap();
        for (PathPattern path : allKeys) {
            // An order is implicit here
            List<ActionBefore> befores = ImmutableList.copyOf(beforeMappings.get(path));
            List<ActionAt> ats = ImmutableList.copyOf(atMappings.get(path));
            List<ActionAfter> afters = ImmutableList.copyOf(afterMappings.get(path));

            ActionBundle mapping = new ActionBundle(befores, ats, afters);
            result.put(path, mapping);
        }

        return ImmutableMap.copyOf(result);
    }

    protected PathMapping createPathMapping(Map<PathPattern, ActionBundle> actionBundleMap,
                                            PatternGroup patternGroup) {
        return new MapBackedPathMapping(actionBundleMap, patternGroup);
    }

    public PathMapping build() {
        Map<PathPattern, ActionBundle> map = buildMap();
        return createPathMapping(map, patternGroupProvider.create(patterns));
    }
}
