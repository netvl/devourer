package org.googolplex.devourer.configuration.modular;

import org.googolplex.devourer.configuration.modular.binders.MappingBinder;

/**
 * This interface is used by Devourer to configure its mappings. You usually should not implement
 * this interface directly but extend {@link AbstractMappingModule} abstract class instead. It provides
 * small DSL to assist in mapping configuration.
 */
public interface MappingModule {
    /**
     * Called to configure Devourer mappings.
     *
     * @param binder an instance of {@link MappingBinder} interface which will contain mapping configuration
     */
    void configure(MappingBinder binder);
}
