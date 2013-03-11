package org.bitbucket.googolplex.devourer.configuration.modular.binders.impl;

import org.bitbucket.googolplex.devourer.configuration.actions.ActionAfter;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAt;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionBefore;
import org.bitbucket.googolplex.devourer.configuration.modular.binders.ActionBindingBuilder;
import org.bitbucket.googolplex.devourer.paths.mappings.MappingBuilder;
import org.bitbucket.googolplex.devourer.paths.patterns.PathPatterns;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Date: 11.03.13
 * Time: 13:14
 */
public class ActionBindingBuilderImplTest {
    @Test
    public void testDoBefore() throws Exception {
        // Prepare
        MappingBuilder mappingBuilder = mock(MappingBuilder.class);
        ActionBefore actionBefore = mock(ActionBefore.class);

        // Run
        ActionBindingBuilder bindingBuilder = new ActionBindingBuilderImpl(mappingBuilder, "/a/b/c");
        ActionBindingBuilder result = bindingBuilder.doBefore(actionBefore);

        // Verify
        assertSame(result, bindingBuilder);
        verify(mappingBuilder).add(PathPatterns.fromString("/a/b/c"), actionBefore);
        verifyNoMoreInteractions(mappingBuilder);
    }

    @Test
    public void testDoAt() throws Exception {
        // Prepare
        MappingBuilder mappingBuilder = mock(MappingBuilder.class);
        ActionAt actionAt = mock(ActionAt.class);

        // Run
        ActionBindingBuilder bindingBuilder = new ActionBindingBuilderImpl(mappingBuilder, "/a/b/c");
        ActionBindingBuilder result = bindingBuilder.doAt(actionAt);

        // Verify
        assertSame(result, bindingBuilder);
        verify(mappingBuilder).add(PathPatterns.fromString("/a/b/c"), actionAt);
        verifyNoMoreInteractions(mappingBuilder);
    }

    @Test
    public void testDoAfter() throws Exception {
        // Prepare
        MappingBuilder mappingBuilder = mock(MappingBuilder.class);
        ActionAfter actionAfter = mock(ActionAfter.class);

        // Run
        ActionBindingBuilder bindingBuilder = new ActionBindingBuilderImpl(mappingBuilder, "/a/b/c");
        ActionBindingBuilder result = bindingBuilder.doAfter(actionAfter);

        // Verify
        assertSame(result, bindingBuilder);
        verify(mappingBuilder).add(PathPatterns.fromString("/a/b/c"), actionAfter);
        verifyNoMoreInteractions(mappingBuilder);
    }
}
