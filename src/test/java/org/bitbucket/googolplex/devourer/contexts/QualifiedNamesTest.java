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

package org.bitbucket.googolplex.devourer.contexts;

import org.junit.Test;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Date: 07.03.13
 * Time: 22:46
 *
 * @author Vladimir Matveev
 */
public class QualifiedNamesTest {
    @Test
    public void testLocalOnly() throws Exception {
        QualifiedName name = QualifiedNames.localOnly("abcd");

        assertNamesEqual(name, "abcd");
    }

    @Test
    public void testWithNamespace() throws Exception {
        QualifiedName name = QualifiedNames.withNamespace("abcd", "urn:some:namespace");

        assertNamesEqual(name, "abcd", "urn:some:namespace");
    }

    @Test
    public void testFull() throws Exception {
        QualifiedName name = QualifiedNames.full("abcd", "urn:some:namespace", "sn");

        assertNamesEqual(name, "abcd", "urn:some:namespace", "sn");
    }

    @Test
    public void testFromQName() throws Exception {
        // Check basic constructors
        QName qName1 = new QName("abcd");
        QualifiedName name1 = QualifiedNames.fromQName(qName1);
        assertNamesEqual(name1, "abcd");

        QName qName2 = new QName("urn:namespace", "abcd");
        QualifiedName name2 = QualifiedNames.fromQName(qName2);
        assertNamesEqual(name2, "abcd", "urn:namespace");

        QName qName3 = new QName("urn:namespace", "abcd", "np");
        QualifiedName name3 = QualifiedNames.fromQName(qName3);
        assertNamesEqual(name3, "abcd", "urn:namespace", "np");

        // Check explicit empty parameters
        QName qName4 = new QName(XMLConstants.NULL_NS_URI, "abcd", XMLConstants.DEFAULT_NS_PREFIX);
        QualifiedName name4 = QualifiedNames.fromQName(qName4);
        assertNamesEqual(name4, "abcd");
    }

    @Test
    public void testFromStringNormal() throws Exception {
        // Check allowed formats
        QualifiedName name1 = QualifiedNames.fromString("abcd");
        assertNamesEqual(name1, "abcd");

        QualifiedName name2 = QualifiedNames.fromString("{urn:namespace}abcd");
        assertNamesEqual(name2, "abcd", "urn:namespace");

        QualifiedName name3 = QualifiedNames.fromString("{urn:namespace}np:abcd");
        assertNamesEqual(name3, "abcd", "urn:namespace", "np");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromStringEmpty() throws Exception {
        QualifiedNames.fromString("");
    }

    @Test(expected = NullPointerException.class)
    public void testFromStringNull() throws Exception {
        QualifiedNames.fromString(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromStringInvalidBraces() throws Exception {
        QualifiedNames.fromString("{urn:namespaceabcd");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromStringOnlyNamespace() throws Exception {
        QualifiedNames.fromString("{urn:namespace}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromStringEmptyNamespace() throws Exception {
        QualifiedNames.fromString("{}abcd");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromStringNamespaceAndPrefixOnly() throws Exception {
        QualifiedNames.fromString("{urn:namespace}prefix:");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromStringPrefixWithoutNamespace() throws Exception {
        QualifiedNames.fromString("prefix:abcd");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromStringEmptyPrefix() throws Exception {
        QualifiedNames.fromString("{urn:namespace}:abcd");
    }

    private void assertNamesEqual(QualifiedName name, String localName) {
        assertEquals(localName, name.localName);
        assertFalse(name.namespace.isPresent());
        assertFalse(name.prefix.isPresent());
    }

    private void assertNamesEqual(QualifiedName name, String localName, String namespace) {
        assertEquals(localName, name.localName);
        assertEquals(namespace, name.namespace.get());
        assertFalse(name.prefix.isPresent());
    }

    private void assertNamesEqual(QualifiedName name, String localName, String namespace, String prefix) {
        assertEquals(localName, name.localName);
        assertEquals(namespace, name.namespace.get());
        assertEquals(prefix, name.prefix.get());
    }


}
