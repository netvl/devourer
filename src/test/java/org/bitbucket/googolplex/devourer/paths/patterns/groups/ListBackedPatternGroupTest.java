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

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.paths.ExactPath;
import org.bitbucket.googolplex.devourer.paths.patterns.PathPattern;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

/**
 * Date: 10.03.13
 * Time: 22:55
 *
 * @author Vladimir Matveev
 */
public class ListBackedPatternGroupTest {
    @Test
    public void testLookupFailure() throws Exception {
        // Prepare
        ExactPath path = ExactPath.fromString("/a/b/c");

        NamespaceContext context = mock(NamespaceContext.class);

        PathPattern p1 = mock(PathPattern.class);
        PathPattern p2 = mock(PathPattern.class);
        PathPattern p3 = mock(PathPattern.class);

        List<PathPattern> patterns = ImmutableList.of(p1, p2, p3);

        // Run
        PatternGroup patternGroup = new ListBackedPatternGroup(patterns);
        Optional<PathPattern> pattern = patternGroup.lookup(path, context);

        // Verify

        // Patterns must be queried in order
        InOrder order = inOrder(p1, p2, p3);
        order.verify(p1).matches(path.parts, context);
        order.verify(p2).matches(path.parts, context);
        order.verify(p3).matches(path.parts, context);
        order.verifyNoMoreInteractions();

        assertFalse(pattern.isPresent());
    }

    @Test
    public void testLookupSuccess() throws Exception {
        // Prepare
        ExactPath path = ExactPath.fromString("/a/b/c");

        NamespaceContext context = mock(NamespaceContext.class);

        PathPattern p1 = mock(PathPattern.class);
        PathPattern p2 = mock(PathPattern.class);
        when(p2.matches(path.parts, context)).thenReturn(true);
        PathPattern p3 = mock(PathPattern.class);

        List<PathPattern> patterns = ImmutableList.of(p1, p2, p3);

        // Run
        PatternGroup patternGroup = new ListBackedPatternGroup(patterns);
        Optional<PathPattern> pattern = patternGroup.lookup(path, context);

        // Verify

        // Patterns must be queried in order
        InOrder order = inOrder(p1, p2, p3);
        order.verify(p1).matches(path.parts, context);
        order.verify(p2).matches(path.parts, context);
        order.verifyNoMoreInteractions();

        assertEquals(p2, pattern.get());
    }

    @Test
    public void testMultipleQueries() throws Exception {
        // Prepare
        ExactPath path = ExactPath.fromString("/a/b/c");

        NamespaceContext context = mock(NamespaceContext.class);

        PathPattern p1 = mock(PathPattern.class);
        PathPattern p2 = mock(PathPattern.class);
        PathPattern p3 = mock(PathPattern.class);

        List<PathPattern> patterns = ImmutableList.of(p1, p2, p3);

        // Run
        PatternGroup patternGroup = new ListBackedPatternGroup(patterns);
        patternGroup.lookup(path, context);
        patternGroup.lookup(path, context);
        patternGroup.lookup(path, context);

        // Verify
        InOrder order = inOrder(p1, p2, p3);
        // First lookup
        order.verify(p1).matches(path.parts, context);
        order.verify(p2).matches(path.parts, context);
        order.verify(p3).matches(path.parts, context);
        // Second lookup
        order.verify(p1).matches(path.parts, context);
        order.verify(p2).matches(path.parts, context);
        order.verify(p3).matches(path.parts, context);
        // Third lookup
        order.verify(p1).matches(path.parts, context);
        order.verify(p2).matches(path.parts, context);
        order.verify(p3).matches(path.parts, context);

        order.verifyNoMoreInteractions();
    }
}
