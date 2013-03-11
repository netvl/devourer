package org.bitbucket.googolplex.devourer.contexts.namespaces;

import com.google.common.base.Optional;
import org.junit.Test;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Date: 11.03.13
 * Time: 15:49
 */
public class QualifiedNameTest {
    @Test
    public void testAsQName() throws Exception {
        QualifiedName name = new QualifiedName("local-name", Optional.<String>absent(), Optional.<String>absent());
        QName qName = name.asQName();
        assertEquals("local-name", qName.getLocalPart());
        assertEquals(XMLConstants.NULL_NS_URI, qName.getNamespaceURI());
        assertEquals(XMLConstants.DEFAULT_NS_PREFIX, qName.getPrefix());

        name = new QualifiedName("local-name", Optional.of("urn:namespace"), Optional.<String>absent());
        qName = name.asQName();
        assertEquals("local-name", qName.getLocalPart());
        assertEquals("urn:namespace", qName.getNamespaceURI());
        assertEquals(XMLConstants.DEFAULT_NS_PREFIX, qName.getPrefix());

        name = new QualifiedName("local-name", Optional.of("urn:namespace"), Optional.of("prefix"));
        qName = name.asQName();
        assertEquals("local-name", qName.getLocalPart());
        assertEquals("urn:namespace", qName.getNamespaceURI());
        assertEquals("prefix", qName.getPrefix());
    }

    @Test
    public void testToString() throws Exception {
        QualifiedName name =
            new QualifiedName("local-name", Optional.<String>absent(), Optional.<String>absent());
        assertEquals("local-name", name.toString());

        name = new QualifiedName("local-name", Optional.of("urn:namespace"), Optional.<String>absent());
        assertEquals("{urn:namespace}local-name", name.toString());

        name = new QualifiedName("local-name", Optional.of("urn:namespace"), Optional.of("prefix"));
        assertEquals("{urn:namespace}prefix:local-name", name.toString());
    }

    @Test
    public void testEquals() throws Exception {
        // Local-only names are equal
        QualifiedName name1 = new QualifiedName("local-name", Optional.<String>absent(), Optional.<String>absent());
        QualifiedName name2 = new QualifiedName("local-name", Optional.<String>absent(), Optional.<String>absent());
        assertTrue(name1.equals(name2));
        assertTrue(name2.equals(name1));

        // Namespaced names are equal
        name1 = new QualifiedName("local-name", Optional.of("urn:ns1"), Optional.<String>absent());
        name2 = new QualifiedName("local-name", Optional.of("urn:ns1"), Optional.<String>absent());
        assertTrue(name1.equals(name2));
        assertTrue(name2.equals(name1));

        // Namespaced names with same prefixes are equal
        name1 = new QualifiedName("local-name", Optional.of("urn:ns1"), Optional.of("prefix"));
        name2 = new QualifiedName("local-name", Optional.of("urn:ns1"), Optional.of("prefix"));
        assertTrue(name1.equals(name2));
        assertTrue(name2.equals(name1));

        // Namespaced names with different prefixes are equal
        name1 = new QualifiedName("local-name", Optional.of("urn:ns1"), Optional.of("prefix"));
        name2 = new QualifiedName("local-name", Optional.of("urn:ns1"), Optional.of("another-prefix"));
        assertTrue(name1.equals(name2));
        assertTrue(name2.equals(name1));

        // Local-only name is different from namespaced
        name1 = new QualifiedName("local-name", Optional.<String>absent(), Optional.<String>absent());
        name2 = new QualifiedName("local-name", Optional.of("urn:ns1"), Optional.<String>absent());
        assertFalse(name1.equals(name2));
        assertFalse(name2.equals(name1));

        // Different local-only names are not equal
        name1 = new QualifiedName("local-name", Optional.<String>absent(), Optional.<String>absent());
        name2 = new QualifiedName("another-name", Optional.<String>absent(), Optional.<String>absent());
        assertFalse(name1.equals(name2));
        assertFalse(name2.equals(name1));

        // Names with different namespaces are not equal
        name1 = new QualifiedName("local-name", Optional.of("urn:ns1"), Optional.<String>absent());
        name2 = new QualifiedName("local-name", Optional.of("urn:ns2"), Optional.<String>absent());
        assertFalse(name1.equals(name2));
        assertFalse(name2.equals(name1));
    }
}
