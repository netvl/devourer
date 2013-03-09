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

package org.bitbucket.googolplex.devourer.paths.patterns;

import com.google.common.collect.ImmutableList;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedName;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedNames;
import org.bitbucket.googolplex.devourer.paths.ExactPath;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Date: 09.03.13
 * Time: 15:08
 *
 * @author Vladimir Matveev
 */
public class LiteralPathPatternTest {
    @Test
    public void testToString() throws Exception {
        PathPattern pattern = PathPatterns.fromString("/a/b/c");
        assertEquals("/a/b/c", pattern.toString());
    }

    @Test
    public void testMatchesEmptyList() throws Exception {
        PathPattern pattern = PathPatterns.fromString("/");

        assertTrue(
            "Empty literal pattern must match empty list",
            pattern.matches(
                ImmutableList.<QualifiedName>of(), NamespaceContext.empty()
            )
        );

        assertFalse(
            "Empty literal pattern must not match empty list",
            pattern.matches(
                ImmutableList.of(QualifiedNames.localOnly("a"), QualifiedNames.localOnly("b")),
                NamespaceContext.empty()
            )
        );
    }

    @Test
    public void testNormalNamesMatch() throws Exception {
        PathPattern pattern = PathPatterns.fromString("/a/b/c");

        assertTrue(
            "Pattern must match exact path",
            pattern.matches(
                ExactPath.fromString("/a/b/c").parts,
                NamespaceContext.empty()
            )
        );

        assertFalse(
            "Pattern must not match different path",
            pattern.matches(
                ExactPath.fromString("/a/b/d").parts,
                NamespaceContext.empty()
            )
        );

        assertFalse(
            "Pattern must not match longer path",
            pattern.matches(
                ExactPath.fromString("/a/b/c/d").parts,
                NamespaceContext.empty()
            )
        );

        assertFalse(
            "Pattern must not match shorter path",
            pattern.matches(
                ExactPath.fromString("/a/b").parts,
                NamespaceContext.empty()
            )
        );

        assertFalse(
            "Pattern must not match path with namespace",
            pattern.matches(
                ExactPath.fromString("/a/{urn:namespace}b/c").parts,
                NamespaceContext.empty()
            )
        );
    }

    @Test
    public void testNamespacesMatch() throws Exception {
        PathPattern pattern = PathPatterns.fromString("/a/p1:b/p2:c");

        assertTrue(
            "Pattern must match path when prefixes for the namespaces are set in the context regardless of " +
            "qualified names own prefixes",
            pattern.matches(
                ExactPath.fromString("/a/{urn:ns1}a1:b/{urn:ns2}a2:c").parts,
                NamespaceContext.fromItems(
                    "urn:ns1", "p1",
                    "urn:ns2", "p2"
                )
            )
        );

        assertFalse(
            "Pattern must not match path when prefixes in the context are incorrect",
            pattern.matches(
                ExactPath.fromString("/a/{urn:ns1}b/{urn:ns2}c").parts,
                NamespaceContext.fromItems(
                    "urn:ns1", "n1",
                    "urn:ns2", "n2"
                )
            )
        );

        assertFalse(
            "Pattern must not match path when prefixes in the context are missing",
            pattern.matches(
                ExactPath.fromString("/a/{urn:ns1}b/{urn:ns2}c").parts,
                NamespaceContext.empty()
            )
        );

        assertFalse(
            "Pattern must not match path when path does not contain required namespaces",
            pattern.matches(
                ExactPath.fromString("/a/b/{urn:ns2}c").parts,
                NamespaceContext.fromItems(
                    "urn:ns1", "p1",
                    "urn:ns2", "p2"
                )
            )
        );
    }

    @Test
    public void testNameWildcardMatch() throws Exception {
        PathPattern pattern = PathPatterns.fromString("/a/*/c");

        assertTrue(
            "Pattern with wildcard must match local name in the path over wildcard part",
            pattern.matches(
                ExactPath.fromString("/a/bde/c").parts,
                NamespaceContext.empty()
            )
        );

        assertFalse(
            "Pattern with wildcard must not match qualified name with namespace in the path over wildcard part",
            pattern.matches(
                ExactPath.fromString("/a/{urn:ns1}b/c").parts,
                NamespaceContext.empty()
            )
        );
    }

    @Test
    public void testPrefixWildcardMatch() throws Exception {
        PathPattern pattern = PathPatterns.fromString("/a/*:b/c");

        assertTrue(
            "Pattern with prefix wildcard must match exact path with any namespace over wildcard part",
            pattern.matches(
                ExactPath.fromString("/a/{urn:something}b/c").parts,
                NamespaceContext.empty()
            )
        );

        assertTrue(
            "Pattern with prefix wildcard must match path with no namespace over wildcard part",
            pattern.matches(
                ExactPath.fromString("/a/b/c").parts,
                NamespaceContext.empty()
            )
        );
    }

    @Test
    public void testFullWildcardMatch() throws Exception {
        PathPattern pattern = PathPatterns.fromString("/a/*:*/c");

        assertTrue(
            "Pattern with full wildcard must match exact path with any local-only name over wildcard part",
            pattern.matches(
                ExactPath.fromString("/a/b/c").parts,
                NamespaceContext.empty()
            )
        );

        assertTrue(
            "Pattern with full wildcard must match exact path with any name with namespace over wildcard part",
            pattern.matches(
                ExactPath.fromString("/a/{ns:ns1}de/c").parts,
                NamespaceContext.empty()
            )
        );

        assertFalse(
            "Pattern with full wildcard must not match longer path",
            pattern.matches(
                ExactPath.fromString("/a/b/b/c").parts,
                NamespaceContext.empty()
            )
        );

        assertFalse(
            "Pattern with full wildcard must not match shorter path",
            pattern.matches(
                ExactPath.fromString("/a/c").parts,
                NamespaceContext.empty()
            )
        );
    }
}
