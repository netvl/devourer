package org.googolplex.devourer.configuration.modular.binders;

import org.googolplex.devourer.paths.Path;
import org.googolplex.devourer.paths.PathMapping;

import java.util.Map;

/**
 * See DSL usage examples in {@link org.googolplex.devourer.configuration.modular.AbstractMappingModule}
 * documentation.
 */
public interface MappingBinder {
    /**
     * See DSL usage examples in {@link org.googolplex.devourer.configuration.modular.AbstractMappingModule}
     * documentation.
     */
    ReactionBindingBuilder on(String route);

    /**
     * See DSL usage examples in {@link org.googolplex.devourer.configuration.modular.AbstractMappingModule}
     * documentation.
     */
    Map<Path, PathMapping> mappings();
}
