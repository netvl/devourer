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

package org.bitbucket.googolplex.devourer.paths;

import com.google.common.collect.ImmutableList;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedName;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedNames;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Date: 11.03.13
 * Time: 0:19
 *
 * @author Vladimir Matveev
 */
public class ExactPathTest {
    @Test
    public void testFromString() throws Exception {
        ExactPath path = ExactPath.fromString("/a/{ns}b/{ns}p:c");

        assertEquals(3, path.parts.size());
        assertEquals(QualifiedNames.localOnly("a"), path.parts.get(0));
        assertEquals(QualifiedNames.withNamespace("b", "ns"), path.parts.get(1));
        assertEquals(QualifiedNames.withNamespace("c", "ns"), path.parts.get(2));
        assertEquals("p", path.parts.get(2).prefix.get());

        path = ExactPath.fromString("/");
        assertTrue(path.parts.isEmpty());

        path = ExactPath.fromString("");
        assertTrue(path.parts.isEmpty());
    }

    @Test
    public void testToString() throws Exception {
        ExactPath path = new ExactPath(ImmutableList.of(
            QualifiedNames.localOnly("a"),
            QualifiedNames.withNamespace("b", "ns"),
            QualifiedNames.full("c", "ns", "p")
        ));

        assertEquals("/a/{ns}b/{ns}p:c", path.toString());

        path = new ExactPath(ImmutableList.<QualifiedName>of());
        assertEquals("/", path.toString());
    }

    @Test
    public void testRoot() throws Exception {
        ExactPath path = ExactPath.root();

        assertTrue(path.parts.isEmpty());
    }

    @Test
    public void testResolve() throws Exception {
        List<QualifiedName> base = ImmutableList.of(
            QualifiedNames.localOnly("a"),
            QualifiedNames.localOnly("b")
        );
        ExactPath path = new ExactPath(base);

        ExactPath newPath = path.resolve(QualifiedNames.localOnly("c"));

        assertEquals(3, newPath.parts.size());
        assertEquals(ImmutableList.<QualifiedName>builder()
                                  .addAll(base).add(QualifiedNames.localOnly("c"))
                                  .build(),
                     newPath.parts);
    }

    @Test
    public void testMoveUp() throws Exception {
        ExactPath path = new ExactPath(ImmutableList.of(
            QualifiedNames.localOnly("a"),
            QualifiedNames.localOnly("b")
        ));

        path = path.moveUp();
        assertEquals(ImmutableList.of(QualifiedNames.localOnly("a")), path.parts);

        path = path.moveUp();
        assertTrue(path.parts.isEmpty());

        path = path.moveUp();
        assertTrue(path.parts.isEmpty());
    }
}
