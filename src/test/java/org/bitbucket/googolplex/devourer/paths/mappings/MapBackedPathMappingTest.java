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
import com.google.common.collect.ImmutableMap;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.paths.ExactPath;
import org.bitbucket.googolplex.devourer.paths.patterns.PathPattern;
import org.bitbucket.googolplex.devourer.paths.patterns.groups.PatternGroup;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Date: 10.03.13
 * Time: 23:31
 *
 * @author Vladimir Matveev
 */
public class MapBackedPathMappingTest {
    @Test
    public void testLookup() throws Exception {
        // Prepare
        ExactPath path1 = ExactPath.fromString("/a/b/c");
        ExactPath path2 = ExactPath.fromString("/d/e/f");
        ExactPath path3 = ExactPath.fromString("/g/h/i");
        ExactPath path4 = ExactPath.fromString("/j/k/l");

        NamespaceContext context = mock(NamespaceContext.class);

        PathPattern pattern1 = mock(PathPattern.class);
        PathPattern pattern2 = mock(PathPattern.class);
        PathPattern pattern3 = mock(PathPattern.class);

        ActionBundle bundle1 = mock(ActionBundle.class);
        ActionBundle bundle2 = mock(ActionBundle.class);

        // path4 is not mapped in the pattern group
        PatternGroup patternGroup = mock(PatternGroup.class);
        when(patternGroup.lookup(any(ExactPath.class), eq(context))).thenReturn(Optional.<PathPattern>absent());
        when(patternGroup.lookup(path1, context)).thenReturn(Optional.of(pattern1));
        when(patternGroup.lookup(path2, context)).thenReturn(Optional.of(pattern2));
        when(patternGroup.lookup(path3, context)).thenReturn(Optional.of(pattern3));

        // pattern3 is not in the map
        Map<PathPattern, ActionBundle> bundleMap = ImmutableMap.of(pattern1, bundle1, pattern2, bundle2);

        // Run
        PathMapping pathMapping = new MapBackedPathMapping(bundleMap, patternGroup);
        Optional<ActionBundle> b1 = pathMapping.lookup(path1, context);
        Optional<ActionBundle> b2 = pathMapping.lookup(path2, context);
        Optional<ActionBundle> b3 = pathMapping.lookup(path3, context);
        Optional<ActionBundle> b4 = pathMapping.lookup(path4, context);

        assertEquals(bundle1, b1.get());
        assertEquals(bundle2, b2.get());
        assertFalse(b3.isPresent());
        assertFalse(b4.isPresent());
    }
}
