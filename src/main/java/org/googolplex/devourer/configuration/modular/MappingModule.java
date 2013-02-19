package org.googolplex.devourer.configuration.modular;

import org.googolplex.devourer.configuration.modular.binders.MappingBinder;

/**
 * Date: 19.02.13
 * Time: 13:51
 */
public interface MappingModule {
    void configure(MappingBinder binder);
}
