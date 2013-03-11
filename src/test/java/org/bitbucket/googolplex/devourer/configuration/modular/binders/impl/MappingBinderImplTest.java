package org.bitbucket.googolplex.devourer.configuration.modular.binders.impl;

import org.bitbucket.googolplex.devourer.configuration.modular.binders.ActionBindingBuilder;
import org.bitbucket.googolplex.devourer.configuration.modular.binders.MappingBinder;
import org.bitbucket.googolplex.devourer.configuration.modular.binders.NamespaceContextBuilder;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.paths.mappings.MappingBuilder;
import org.bitbucket.googolplex.devourer.paths.mappings.PathMapping;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Date: 11.03.13
 * Time: 14:48
 */
public class MappingBinderImplTest {
    @Test
    public void testOn() throws Exception {
        // Prepare
        MappingBuilder mappingBuilder = mock(MappingBuilder.class);

        NamespaceContext.Builder namespaceContextBuilder = mock(NamespaceContext.Builder.class);

        // Run
        MappingBinder mappingBinder = new MappingBinderImpl(mappingBuilder,  namespaceContextBuilder);
        ActionBindingBuilder result = mappingBinder.on("/a/b/c");

        // Verify
        verifyZeroInteractions(mappingBuilder, namespaceContextBuilder);
        assertTrue(result instanceof ActionBindingBuilderImpl);
        ActionBindingBuilderImpl impl = (ActionBindingBuilderImpl) result;
        assertEquals(mappingBuilder, impl.mappingBuilder);
        assertEquals("/a/b/c", impl.route);
    }

    @Test
    public void testNamespaceContext() throws Exception {
        // Prepare
        MappingBuilder mappingBuilder = mock(MappingBuilder.class);

        NamespaceContext.Builder namespaceContextBuilder = mock(NamespaceContext.Builder.class);

        // Run
        MappingBinder mappingBinder = new MappingBinderImpl(mappingBuilder, namespaceContextBuilder);
        NamespaceContextBuilder result = mappingBinder.namespaceContext();

        // Verify
        verifyZeroInteractions(mappingBuilder, namespaceContextBuilder);
        assertTrue(result instanceof NamespaceContextBuilderImpl);
        NamespaceContextBuilderImpl impl = (NamespaceContextBuilderImpl) result;
        assertEquals(namespaceContextBuilder, impl.namespaceContextBuilder);
    }

    @Test
    public void testGetMapping() throws Exception {
        // Prepare
        PathMapping mapping = mock(PathMapping.class);

        MappingBuilder mappingBuilder = mock(MappingBuilder.class);
        when(mappingBuilder.build()).thenReturn(mapping);

        NamespaceContext.Builder namespaceContextBuilder = mock(NamespaceContext.Builder.class);

        // Run
        MappingBinder mappingBinder = new MappingBinderImpl(mappingBuilder, namespaceContextBuilder);
        PathMapping result = mappingBinder.getMapping();

        // Verify
        verify(mappingBuilder).build();
        verifyNoMoreInteractions(mappingBuilder);
        verifyZeroInteractions(namespaceContextBuilder);

        assertEquals(mapping, result);
    }

    @Test
    public void testGetNamespaceContext() throws Exception {
        // Prepare
        NamespaceContext namespaceContext = mock(NamespaceContext.class);

        MappingBuilder mappingBuilder = mock(MappingBuilder.class);

        NamespaceContext.Builder namespaceContextBuilder = mock(NamespaceContext.Builder.class);
        when(namespaceContextBuilder.build()).thenReturn(namespaceContext);

        // Run
        MappingBinder mappingBinder = new MappingBinderImpl(mappingBuilder, namespaceContextBuilder);
        NamespaceContext result = mappingBinder.getNamespaceContext();

        // Verify
        verify(namespaceContextBuilder).build();
        verifyNoMoreInteractions(namespaceContextBuilder);
        verifyZeroInteractions(mappingBuilder);

        assertEquals(namespaceContext, result);
    }
}
