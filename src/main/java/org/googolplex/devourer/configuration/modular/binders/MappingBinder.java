package org.googolplex.devourer.configuration.modular.binders;

import org.googolplex.devourer.paths.Path;
import org.googolplex.devourer.paths.PathMapping;

import java.util.Map;

/**
 * Date: 19.02.13
 * Time: 13:51
 */
public interface MappingBinder {
    ReactionBindingBuilder on(String route);
    Map<Path, PathMapping> mappings();
}
