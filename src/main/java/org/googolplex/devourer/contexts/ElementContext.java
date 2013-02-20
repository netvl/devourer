package org.googolplex.devourer.contexts;

import com.google.common.base.Optional;

/**
 * Element context contains information about the current element. It is provided to reactions configured
 * in {@link org.googolplex.devourer.configuration.modular.MappingModule}s or to annotated methods in annotated
 * configuration object.
 */
public interface ElementContext {
    /**
     * @return local name of the currently processed element
     */
    String elementName();

    /**
     * @return namespace prefix of the currently processed element, if present
     */
    Optional<String> elementPrefix();
}
