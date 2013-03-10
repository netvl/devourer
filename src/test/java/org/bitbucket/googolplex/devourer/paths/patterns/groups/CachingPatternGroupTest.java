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
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.paths.ExactPath;
import org.bitbucket.googolplex.devourer.paths.patterns.PathPattern;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

/**
 * Date: 10.03.13
 * Time: 23:21
 *
 * @author Vladimir Matveev
 */
public class CachingPatternGroupTest {
    @Test
    public void testMultipleInteractions() throws Exception {
        // Prepare
        ExactPath path1 = ExactPath.fromString("/a/b/c");
        ExactPath path2 = ExactPath.fromString("/d/e/f");

        NamespaceContext context = mock(NamespaceContext.class);

        PathPattern pattern = mock(PathPattern.class);

        PatternGroup inner = mock(PatternGroup.class);
        when(inner.lookup(path1, context)).thenReturn(Optional.<PathPattern>absent());
        when(inner.lookup(path2, context)).thenReturn(Optional.of(pattern));

        // Run
        PatternGroup patternGroup = new CachingPatternGroup(inner);
        Optional<PathPattern> p1 = patternGroup.lookup(path1, context);
        Optional<PathPattern> p2 = patternGroup.lookup(path1, context);
        Optional<PathPattern> p3 = patternGroup.lookup(path2, context);
        Optional<PathPattern> p4 = patternGroup.lookup(path2, context);

        // Verify
        // lookup on inner class should be called only once for each path
        verify(inner).lookup(path1, context);
        verify(inner).lookup(path2, context);
        verifyNoMoreInteractions(inner);

        assertFalse(p1.isPresent());
        assertFalse(p2.isPresent());
        assertEquals(pattern, p3.get());
        assertEquals(pattern, p4.get());
    }
}
