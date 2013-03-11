package org.bitbucket.googolplex.devourer.configuration.modular.binders.impl;

import org.bitbucket.googolplex.devourer.configuration.modular.binders.NamespaceContextBuilder;
import org.bitbucket.googolplex.devourer.configuration.modular.binders.NamespaceContextMappingBuilder;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Date: 11.03.13
 * Time: 15:08
 */
public class NamespaceContextMappingBuilderImplTest {
    @Test
    public void testTo() throws Exception {
        // Prepare
        NamespaceContextBuilder namespaceContextBuilder = mock(NamespaceContextBuilder.class);

        NamespaceContext.Builder namespaceContext = mock(NamespaceContext.Builder.class);

        // Run
        NamespaceContextMappingBuilder namespaceContextMappingBuilder =
            new NamespaceContextMappingBuilderImpl(namespaceContextBuilder, namespaceContext, "urn:namespace");
        NamespaceContextBuilder result = namespaceContextMappingBuilder.to("prefix");

        // Verify
        assertEquals(namespaceContextBuilder, result);
        verify(namespaceContext).add("urn:namespace", "prefix");
        verifyNoMoreInteractions(namespaceContext);
        verifyZeroInteractions(namespaceContextBuilder);
    }
}
