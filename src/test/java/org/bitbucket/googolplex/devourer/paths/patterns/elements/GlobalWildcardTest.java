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

import com.google.common.collect.ImmutableList;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedName;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedNames;
import org.bitbucket.googolplex.devourer.paths.PathsConstants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Date: 09.03.13
 * Time: 14:42
 *
 * @author Vladimir Matveev
 */
public class GlobalWildcardTest {
    @Test
    public void testMatches() throws Exception {
        GlobalWildcard wildcard = GlobalWildcard.get();

        assertTrue(
            "Global wildcard must match empty list",
            wildcard.matches(
                ImmutableList.<QualifiedName>of(), NamespaceContext.empty()
            )
        );

        assertTrue(
            "Global wildcard must match any non-empty list",
            wildcard.matches(
                ImmutableList.of(
                    QualifiedNames.localOnly("a"),
                    QualifiedNames.localOnly("b"),
                    QualifiedNames.localOnly("c")
                ),
                NamespaceContext.empty()
            )
        );
    }

    @Test
    public void testType() throws Exception {
        GlobalWildcard wildcard = GlobalWildcard.get();
        assertTrue(wildcard.isGlobalWildcard());
        assertFalse(wildcard.isLiteral());
    }

    @Test
    public void testToString() throws Exception {
        GlobalWildcard wildcard = GlobalWildcard.get();
        assertEquals(PathsConstants.GLOBAL_WILDCARD, wildcard.toString());
    }
}
