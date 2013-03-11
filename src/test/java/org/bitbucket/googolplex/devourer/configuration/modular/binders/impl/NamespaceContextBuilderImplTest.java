package org.bitbucket.googolplex.devourer.configuration.modular.binders.impl;

import org.bitbucket.googolplex.devourer.configuration.modular.binders.NamespaceContextBuilder;
import org.bitbucket.googolplex.devourer.configuration.modular.binders.NamespaceContextMappingBuilder;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Date: 11.03.13
 * Time: 15:03
 */
public class NamespaceContextBuilderImplTest {
    @Test
    public void testMap() throws Exception {
        // Prepare
        NamespaceContext.Builder builder = mock(NamespaceContext.Builder.class);

        // Run
        NamespaceContextBuilder namespaceContextBuilder = new NamespaceContextBuilderImpl(builder);
        NamespaceContextMappingBuilder result = namespaceContextBuilder.map("urn:namespace");

        // Verify
        verifyZeroInteractions(builder);
        assertTrue(result instanceof NamespaceContextMappingBuilderImpl);
        NamespaceContextMappingBuilderImpl impl = (NamespaceContextMappingBuilderImpl) result;
        assertEquals(namespaceContextBuilder, impl.parent);
        assertEquals(builder, impl.builder);
        assertEquals("urn:namespace", impl.namespace);
    }

    @Test
    public void testUseAllFrom() throws Exception {
        // Prepare
        NamespaceContext.Builder builder = mock(NamespaceContext.Builder.class);
        NamespaceContext context = mock(NamespaceContext.class);

        // Run
        NamespaceContextBuilder namespaceContextBuilder = new NamespaceContextBuilderImpl(builder);
        NamespaceContextBuilder result = namespaceContextBuilder.useAllFrom(context);

        // Verify
        assertEquals(result, namespaceContextBuilder);
        verify(builder).addFrom(context);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(context);
    }
}
