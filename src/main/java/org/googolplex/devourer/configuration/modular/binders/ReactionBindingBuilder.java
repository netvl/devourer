package org.googolplex.devourer.configuration.modular.binders;

import org.googolplex.devourer.configuration.reactions.ReactionAfter;
import org.googolplex.devourer.configuration.reactions.ReactionAt;
import org.googolplex.devourer.configuration.reactions.ReactionBefore;

/**
 * See DSL usage examples in {@link org.googolplex.devourer.configuration.modular.AbstractMappingModule}
 * documentation.
 */
public interface ReactionBindingBuilder {
    /**
     * See DSL usage examples in {@link org.googolplex.devourer.configuration.modular.AbstractMappingModule}
     * documentation.
     */
    ReactionBindingBuilder doBefore(ReactionBefore reaction);

    /**
     * See DSL usage examples in {@link org.googolplex.devourer.configuration.modular.AbstractMappingModule}
     * documentation.
     */
    ReactionBindingBuilder doAt(ReactionAt reaction);

    /**
     * See DSL usage examples in {@link org.googolplex.devourer.configuration.modular.AbstractMappingModule}
     * documentation.
     */
    ReactionBindingBuilder doAfter(ReactionAfter reaction);
}
