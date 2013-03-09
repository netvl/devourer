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
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Date: 09.03.13
 * Time: 13:41
 *
 * @author Vladimir Matveev
 */
public class LiteralNameTest {
    @Test
    public void testType() throws Exception {
        LiteralName name = LiteralName.localOnly("abcd");
        assertTrue(name.isLiteral());
        assertFalse(name.isGlobalWildcard());
    }

    @Test
    public void testLocalOnly() throws Exception {
        LiteralName name = LiteralName.localOnly("local-name");
        assertEquals("local-name", name.name);
        assertFalse(name.prefix.isPresent());
    }

    @Test
    public void testWithPrefix() throws Exception {
        LiteralName name = LiteralName.withPrefix("local-name", "prefix");
        assertEquals("local-name", name.name);
        assertEquals("prefix", name.prefix.get());
    }

    @Test
    public void testFromString() throws Exception {
        LiteralName name = LiteralName.fromString("local-only");
        assertEquals("local-only", name.name);
        assertFalse(name.prefix.isPresent());

        name = LiteralName.fromString("pref:local");
        assertEquals("local", name.name);
        assertEquals("pref", name.prefix.get());
    }

    @Test
    public void testToString() throws Exception {
        LiteralName name = LiteralName.localOnly("local-only");
        assertEquals("local-only", name.toString());

        name = LiteralName.withPrefix("local", "prefix");
        assertEquals("prefix:local", name.toString());
    }

    @Test
    public void testMatchesEmptyList() throws Exception {
        LiteralName name = LiteralName.withPrefix("name", "prefix");

        assertFalse(
            "Literal name must not match empty list",
            name.matches(
                ImmutableList.<QualifiedName>of(),
                NamespaceContext.empty()
            )
        );
    }

    @Test
    public void testMatchesTooLongList() throws Exception {
        LiteralName name = LiteralName.withPrefix("name", "prefix");

        assertFalse(
            "Literal name must not match a list with more than 1 element",
            name.matches(
                ImmutableList.of(QualifiedNames.localOnly("a"), QualifiedNames.localOnly("b")),
                NamespaceContext.empty()
            )
        );
    }

    @Test
    public void testMatchesNameOnly() throws Exception {
        LiteralName name = LiteralName.localOnly("name");

        assertTrue(
            "Literal name without prefix must match qualified name without namespace",
            name.matches(
                ImmutableList.of(QualifiedNames.localOnly("name")),
                NamespaceContext.empty()
            )
        );

        assertFalse(
            "Literal name without prefix must not match qualified name with not matching local name",
            name.matches(
                ImmutableList.of(QualifiedNames.localOnly("another-name")),
                NamespaceContext.empty()
            )
        );

        assertFalse(
            "Literal name without prefix must not match qualified name with namespace",
            name.matches(
                ImmutableList.of(QualifiedNames.withNamespace("name", "urn:namespace")),
                NamespaceContext.empty()
            )
        );
    }

    @Test
    public void testMatchesNameWildcardWithoutPrefix() throws Exception {
        LiteralName name = LiteralName.localOnly("*");

        assertTrue(
            "Literal wildcard name without prefix must match any qualified name without namespace",
            name.matches(
                ImmutableList.of(QualifiedNames.localOnly("name")),
                NamespaceContext.empty()
            )
        );
        assertFalse(
            "Literal wildcard name without prefix must not match qualified name with namespace",
            name.matches(
                ImmutableList.of(QualifiedNames.withNamespace("name", "urn:namespace")),
                NamespaceContext.empty()
            )
        );
    }

    @Test
    public void testMatchesWithPrefix() throws Exception {
        LiteralName name = LiteralName.withPrefix("name", "p");

        assertTrue(
            "Literal name with prefix must match qualified name with prefix set in the context, with no regard to " +
            "qualified name's own prefix",
            name.matches(
                ImmutableList.of(QualifiedNames.full("name", "urn:namespace", "n")),
                NamespaceContext.fromItems("urn:namespace", "p")
            )
        );

        assertTrue(
            "Literal name with prefix must match qualified name with prefix set in the context even without " +
            "qualified name's own prefix",
            name.matches(
                ImmutableList.of(QualifiedNames.withNamespace("name", "urn:namespace")),
                NamespaceContext.fromItems("urn:namespace", "p")
            )
        );

        assertFalse(
            "Literal name with prefix must not match qualified name with not matching local name",
            name.matches(
                ImmutableList.of(QualifiedNames.withNamespace("another-name", "urn:namespace")),
                NamespaceContext.fromItems("urn:namespace", "p")
            )
        );

        assertFalse(
            "Literal name with prefix must not match qualified name without namespace",
            name.matches(
                ImmutableList.of(QualifiedNames.localOnly("name")),
                NamespaceContext.empty()
            )
        );

        assertFalse(
            "Literal name with prefix must not match qualified name with not matching prefix set in the context, " +
            "even if qualified name's own prefix matches literal name's prefix",
            name.matches(
                ImmutableList.of(QualifiedNames.full("name", "urn:namespace", "p")),
                NamespaceContext.fromItems("urn:namespace", "n")
            )
        );
    }

    @Test
    public void testMatchesFullWildcard() throws Exception {
        LiteralName name = LiteralName.withPrefix("*", "*");

        assertTrue(
            "Full wildcard literal name must match any qualified name without namespace",
            name.matches(
                ImmutableList.of(QualifiedNames.localOnly("name")),
                NamespaceContext.empty()
            )
        );

        assertTrue(
            "Full wildcard literal name must match any full qualified name",
            name.matches(
                ImmutableList.of(QualifiedNames.full("name", "urn:namespace", "n")),
                NamespaceContext.fromItems("urn:namespace", "p")
            )
        );

        assertTrue(
            "Full wildcard literal name must match any full qualified name even with empty context",
            name.matches(
                ImmutableList.of(QualifiedNames.full("name", "urn:namespace", "n")),
                NamespaceContext.empty()
            )
        );
    }

    @Test
    public void testMatchesPrefixWildcard() throws Exception {
        LiteralName name = LiteralName.withPrefix("name", "*");

        assertTrue(
            "Literal name with prefix wildcard must match qualified name without namespace",
            name.matches(
                ImmutableList.of(QualifiedNames.localOnly("name")),
                NamespaceContext.empty()
            )
        );

        assertTrue(
            "Literal name with prefix wildcard must match qualified name with prefix set in the context",
            name.matches(
                ImmutableList.of(QualifiedNames.withNamespace("name", "urn:namespace")),
                NamespaceContext.fromItems("urn:namespace", "p")
            )
        );

        assertTrue(
            "Literal name with prefix wildcard must match qualified name with namespace even with empty context",
            name.matches(
                ImmutableList.of(QualifiedNames.withNamespace("name", "urn:namespace")),
                NamespaceContext.empty()
            )
        );

        assertFalse(
            "Literal name with prefix wildcard must not match qualified name with not matching local name",
            name.matches(
                ImmutableList.of(QualifiedNames.withNamespace("another-name", "urn:namespace")),
                NamespaceContext.fromItems("urn:namespace", "p")
            )
        );
    }

    @Test
    public void testMatchesNameWildcardWithPrefix() throws Exception {
        LiteralName name = LiteralName.withPrefix("*", "p");

        assertTrue(
            "Literal wildcard name with prefix must match any qualified name with correct prefix set in the context",
            name.matches(
                ImmutableList.of(QualifiedNames.withNamespace("name", "urn:namespace")),
                NamespaceContext.fromItems("urn:namespace", "p")
            )
        );

        assertTrue(
            "Literal wildcard name with prefix must match any qualified name with correct prefix set in the " +
            "context with no regard to qualified name's own prefix",
            name.matches(
                ImmutableList.of(QualifiedNames.full("name", "urn:namespace", "n")),
                NamespaceContext.fromItems("urn:namespace", "p")
            )
        );


        assertFalse(
            "Literal wildcard name with prefix must not match qualified name without namespace",
            name.matches(
                ImmutableList.of(QualifiedNames.localOnly("name")),
                NamespaceContext.empty()
            )
        );

        assertFalse(
            "Literal wildcard name with prefix must not match qualified name with not matching prefix set in " +
            "the context, even if qualified name's own prefix matches literal name's prefix",
            name.matches(
                ImmutableList.of(QualifiedNames.full("name", "urn:namespace", "p")),
                NamespaceContext.fromItems("urn:namespace", "n")
            )
        );
    }
}

