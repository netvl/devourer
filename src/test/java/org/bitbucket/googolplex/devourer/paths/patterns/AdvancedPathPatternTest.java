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
import org.bitbucket.googolplex.devourer.paths.ExactPath;
import org.bitbucket.googolplex.devourer.paths.patterns.elements.PatternElements;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Date: 09.03.13
 * Time: 21:41
 *
 * @author Vladimir Matveev
 */
public class AdvancedPathPatternTest {
    @Test
    public void testToString() throws Exception {
        PathPattern pattern = AdvancedPathPattern.fromList(
            ImmutableList.of(
                PatternElements.literal("a"),
                PatternElements.globalWildcard(),
                PatternElements.literal("b", "p")
            )
        );
        assertEquals("/a/**/p:b", pattern.toString());
    }
    @Test
    public void testMatchesEmptyList() throws Exception {
        PathPattern pattern = PathPatterns.fromString("/");

        checkMatch(pattern, "/",
                   "Empty literal pattern must match empty list");

        checkNoMatch(pattern, "/a/b",
                     "Empty literal pattern must not match non-empty list");
    }

    @Test
    public void testNormalNamesMatch() throws Exception {
        PathPattern pattern = PathPatterns.fromString("/a/b/c");

        checkMatch(pattern, "/a/b/c",
                   "Pattern must match exact path");

        checkNoMatch(pattern, "/a/b/d",
                     "Pattern must not match different path");

        checkNoMatch(pattern, "/a/b/c/d",
                     "Pattern must not match longer path");

        checkNoMatch(pattern, "/a/b",
                     "Pattern must not match shorter path");

        checkNoMatch(pattern, "/a/{urn:namespace}b/c",
                     "Pattern must not match path with namespace");
    }

    @Test
    public void testNamespacesMatch() throws Exception {
        PathPattern pattern = PathPatterns.fromString("/a/p1:b/p2:c");

        checkMatch(pattern, "/a/{urn:ns1}a1:b/{urn:ns2}a2:c",
                   NamespaceContext.fromItems("urn:ns1", "p1", "urn:ns2", "p2"),
                   "Pattern must match path when prefixes for the namespaces are set in the context regardless of " +
                   "qualified names own prefixes");

        checkNoMatch(pattern, "/a/{urn:ns1}b/{urn:ns2}c",
                     NamespaceContext.fromItems("urn:ns1", "n1", "urn:ns2", "n2"),
                     "Pattern must not match path when prefixes in the context are incorrect");

        checkNoMatch(pattern, "/a/{urn:ns1}b/{urn:ns2}c",
                     "Pattern must not match path when prefixes in the context are missing");

        checkNoMatch(pattern, "/a/b/{urn:ns2}c",
                     NamespaceContext.fromItems("urn:ns1", "p1", "urn:ns2", "p2"),
                     "Pattern must not match path when path does not contain required namespaces");
    }

    @Test
    public void testNameWildcardMatch() throws Exception {
        PathPattern pattern = PathPatterns.fromString("/a/*/c");

        checkMatch(pattern, "/a/bde/c",
                   "Pattern with wildcard must match local name in the path over wildcard part");

        checkNoMatch(pattern, "/a/{urn:something}b/c",
                     "Pattern with wildcard must not match qualified name with namespace in the path over " +
                     "wildcard part");
    }

    @Test
    public void testPrefixWildcardMatch() throws Exception {
        PathPattern pattern = PathPatterns.fromString("/a/*:b/c");

        checkMatch(pattern, "/a/{urn:something}b/c",
                   "Pattern with prefix wildcard must match exact path with any namespace over wildcard part");

        checkMatch(pattern, "/a/b/c",
                   "Pattern with prefix wildcard must match path with no namespace over wildcard part");
    }

    @Test
    public void testFullWildcardMatch() throws Exception {
        PathPattern pattern = PathPatterns.fromString("/a/*:*/c");

        checkMatch(pattern, "/a/b/c",
                   "Pattern with full wildcard must match exact path with any local-only name over wildcard part");

        checkMatch(pattern, "/a/{ns:ns1}de/c",
                   "Pattern with full wildcard must match exact path with any name with namespace over wildcard part");

        checkNoMatch(pattern, "/a/b/b/c",
                     "Pattern with full wildcard must not match longer path");

        checkNoMatch(pattern, "/a/c",
                     "Pattern with full wildcard must not match shorter path");
    }

    @Test
    public void testGlobalWildcardInTheMiddleMatches() throws Exception {
        PathPattern pattern = PathPatterns.fromString("/a/**/b");

        checkMatch(pattern, "/a/b",
                   "Pattern with global wildcard must match zero elements over global wildcard");

        checkMatch(pattern, "/a/c/b",
                   "Pattern with global wildcard must match one element over global wildcard");

        checkMatch(pattern, "/a/b/c/d/e/b",
                   "Pattern with global wildcard must match more than one element over global wildcard");

        checkNoMatch(pattern, "/c/b",
                     "Pattern with global wildcard must not match path with not matching elements before wildcard");

        checkNoMatch(pattern, "/a/b/d",
                     "Pattern with global wildcard must not match path with not matching elements after wildcard");
    }

    @Test
    public void testGlobalWildcardAtTheEndMatches() throws Exception {
        PathPattern pattern = PathPatterns.fromString("/a/b/**");

        checkMatch(pattern, "/a/b",
                   "Pattern with global wildcard must match zero elements over global wildcard");

        checkMatch(pattern, "/a/b/c",
                   "Pattern with global wildcard must match one element over global wildcard");

        checkMatch(pattern, "/a/b/c/d/e/f/g",
                   "Pattern with global wildcard must match more than one element over global wildcard");

        checkNoMatch(pattern, "/a/c/d",
                     "Pattern with global wildcard must not match path with not matching elements before wildcard");
    }

    @Test
    public void testGlobalWildcardAtTheBeginning() throws Exception {
        PathPattern pattern = PathPatterns.fromString("**/a/b");

        checkMatch(pattern, "/a/b",
                   "Pattern with global wildcard must match zero elements over global wildcard");

        checkMatch(pattern, "/c/a/b",
                   "Pattern with global wildcard must match one element over global wildcard");

        checkMatch(pattern, "/l/s/d/f/s/a/b",
                   "Pattern with global wildcard must match more than one element over global wildcard");

        checkNoMatch(pattern, "/c/d/e/z/y",
                     "Pattern with global wildcard must not match path with not matching elements after wildcard");
    }

    @Test
    public void testSeveralGlobalWildcards() throws Exception {
        PathPattern pattern = PathPatterns.fromString("/a/b/**/p:c/p:d/**/e/f");

        checkMatch(pattern, "/a/b/{urn:ns1}c/{urn:ns1}d/e/f",
                   NamespaceContext.fromItems("urn:ns1", "p"),
                   "Pattern must match zero elements over global wildcards");

        checkMatch(pattern, "/a/b/z/{urn:ns1}c/{urn:ns1}d/x/e/f",
                   NamespaceContext.fromItems("urn:ns1", "p"),
                   "Pattern must match one element over each global wildcard");

        checkMatch(pattern, "/a/b/z/z/z/{urn:ns1}c/{urn:ns1}d/x/x/x/e/f",
                   NamespaceContext.fromItems("urn:ns1", "p"),
                   "Pattern must match more than one element over each global wildcard");

        checkNoMatch(pattern, "/z/z/{urn:ns1}c/{urn:ns1}d/x/e/f",
                     NamespaceContext.fromItems("urn:ns1", "p"),
                     "Pattern must not match path with not matching elements before wildcard");

        checkNoMatch(pattern, "/a/b/z/z/{urn:ns1}c/{urn:ns1}d/x/x/x",
                     NamespaceContext.fromItems("urn:ns1", "p"),
                     "Pattern must not match path with not matching elements after wildcard");

        checkNoMatch(pattern, "/a/b/z/m/n/x/e/f",
                     NamespaceContext.fromItems("urn:ns1", "p"),
                     "Pattern must not match path with not matching elements between wildcards");

        checkNoMatch(pattern, "/a/b/z/x/e/f",
                     NamespaceContext.fromItems("urn:ns1", "p"),
                     "Pattern must not match path with no elements between wildcards when there should be such " +
                     "elements");
    }

    @Test
    public void testSingleGlobalWildcard() throws Exception {
        PathPattern pattern = PathPatterns.fromString("/**");

        checkMatch(pattern, "/",
                   "Pattern must match empty path");

        checkMatch(pattern, "/a",
                   "Pattern must match single element");

        checkMatch(pattern, "/a/b/c/d/e",
                   "Pattern must match more than one element");
    }

    private void checkMatch(PathPattern pattern, String path, NamespaceContext context, String message) {
        assertTrue(
            message,
            pattern.matches(
                ExactPath.fromString(path).parts,
                context
            )
        );
    }

    private void checkNoMatch(PathPattern pattern, String path, NamespaceContext context, String message) {
        assertFalse(
            message,
            pattern.matches(
                ExactPath.fromString(path).parts,
                context
            )
        );
    }

    private void checkMatch(PathPattern pattern, String path, String message) {
        checkMatch(pattern, path, NamespaceContext.empty(), message);
    }

    private void checkNoMatch(PathPattern pattern, String path, String message) {
        checkNoMatch(pattern, path, NamespaceContext.empty(), message);
    }
}
