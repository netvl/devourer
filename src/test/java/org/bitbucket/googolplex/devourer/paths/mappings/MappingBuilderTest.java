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

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAfter;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAt;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionBefore;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.paths.ExactPath;
import org.bitbucket.googolplex.devourer.paths.patterns.PathPattern;
import org.bitbucket.googolplex.devourer.paths.patterns.groups.PatternGroup;
import org.bitbucket.googolplex.devourer.paths.patterns.groups.PatternGroupProvider;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Date: 10.03.13
 * Time: 23:44
 *
 * @author Vladimir Matveev
 */
public class MappingBuilderTest {
    @Test
    public void testBuild() throws Exception {
        // Prepare
        PatternGroup patternGroup = mock(PatternGroup.class);

        PathPattern p1 = mock(PathPattern.class);
        PathPattern p2 = mock(PathPattern.class);
        PathPattern p3 = mock(PathPattern.class);

        ActionBefore a11 = mock(ActionBefore.class);
        ActionAt a12 = mock(ActionAt.class);
        ActionAfter a13 = mock(ActionAfter.class);

        ActionBefore a21 = mock(ActionBefore.class);
        ActionAt a22 = mock(ActionAt.class);
        ActionAfter a23 = mock(ActionAfter.class);

        ActionBefore a31 = mock(ActionBefore.class);
        ActionAt a32 = mock(ActionAt.class);
        ActionAfter a33 = mock(ActionAfter.class);

        PatternGroupProviderStub patternGroupProvider = new PatternGroupProviderStub(patternGroup);

        // Run
        MappingBuilder mappingBuilder = createMappingBuilder();
        PathMapping mapping =
            mappingBuilder
                .withPatternGroupProvider(patternGroupProvider)
                .add(p1, a11)
                .add(p1, a12)
                .add(p1, a13)
                .add(p2, a21)
                .add(p2, a22)
                .add(p2, a23)
                .add(p3, a31)
                .add(p3, a32)
                .add(p3, a33)
                .build();

        // Verify
        PathMappingStub stub = (PathMappingStub) mapping;
        assertEquals(patternGroup, stub.patternGroup);

        assertEquals(3, stub.actionBundleMap.size());

        ActionBundle bundle = stub.actionBundleMap.get(p1);
        assertEquals(ImmutableList.of(a11), bundle.befores);
        assertEquals(ImmutableList.of(a12), bundle.ats);
        assertEquals(ImmutableList.of(a13), bundle.afters);

        bundle = stub.actionBundleMap.get(p2);
        assertEquals(ImmutableList.of(a21), bundle.befores);
        assertEquals(ImmutableList.of(a22), bundle.ats);
        assertEquals(ImmutableList.of(a23), bundle.afters);

        bundle = stub.actionBundleMap.get(p3);
        assertEquals(ImmutableList.of(a31), bundle.befores);
        assertEquals(ImmutableList.of(a32), bundle.ats);
        assertEquals(ImmutableList.of(a33), bundle.afters);

        assertEquals(ImmutableList.of(p1, p2, p3), ImmutableList.copyOf(patternGroupProvider.patterns));
    }

    private MappingBuilder createMappingBuilder() {
        return new MappingBuilder() {
            @Override
            protected PathMapping createPathMapping(Map<PathPattern, ActionBundle> actionBundleMap,
                                                    PatternGroup patternGroup) {
                return new PathMappingStub(actionBundleMap, patternGroup);
            }
        };
    }

    private static class PatternGroupProviderStub implements PatternGroupProvider {
        private final PatternGroup object;
        private Iterable<? extends PathPattern> patterns;

        private PatternGroupProviderStub(PatternGroup object) {
            this.object = object;
        }

        @Override
        public PatternGroup create(Iterable<? extends PathPattern> patterns) {
            this.patterns = patterns;
            return object;
        }
    }

    private static class PathMappingStub implements PathMapping {
        private final Map<PathPattern, ActionBundle> actionBundleMap;
        private final PatternGroup patternGroup;

        private PathMappingStub(Map<PathPattern, ActionBundle> actionBundleMap, PatternGroup patternGroup) {
            this.actionBundleMap = actionBundleMap;
            this.patternGroup = patternGroup;
        }

        @Override
        public Optional<ActionBundle> lookup(ExactPath path, NamespaceContext namespaceContext) {
            return null;
        }
    }

}
