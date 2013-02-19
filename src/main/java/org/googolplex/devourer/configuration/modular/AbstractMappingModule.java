package org.googolplex.devourer.configuration.modular;

import com.google.common.base.Preconditions;
import org.googolplex.devourer.configuration.modular.binders.ReactionBindingBuilder;
import org.googolplex.devourer.configuration.modular.binders.MappingBinder;

/**
 * Date: 19.02.13
 * Time: 13:55
 */
public abstract class AbstractMappingModule implements MappingModule {
    private MappingBinder binder = null;

    protected final ReactionBindingBuilder map(String route) {
        return binder.map(route);
    }

    @Override
    public synchronized final void configure(MappingBinder binder) {
        Preconditions.checkState(this.binder == null, "Cannot re-enter configuration process");
        Preconditions.checkNotNull(binder, "Binder is null");

        this.binder = binder;
        try {
            configure();
        } finally {
            this.binder = null;
        }
    }

    protected abstract void configure();
}
