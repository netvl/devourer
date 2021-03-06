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

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Date: 09.03.13
 * Time: 14:33
 *
 * @author Vladimir Matveev
 */
public class PatternElementsTest {
    @Test
    public void testLiteral() throws Exception {
        PatternElement element = PatternElements.literal("abcd");
        assertTrue(element.isLiteral());
        assertFalse(element.isGlobalWildcard());

        element = PatternElements.literal("abcd", "p");
        assertTrue(element.isLiteral());
        assertFalse(element.isGlobalWildcard());
    }

    @Test
    public void testMultiWildcard() throws Exception {
        PatternElement element = PatternElements.globalWildcard();
        assertTrue(element.isGlobalWildcard());
        assertFalse(element.isLiteral());
    }
}
