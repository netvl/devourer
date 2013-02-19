package org.googolplex.devourer.configuration.modular.binders;

import org.googolplex.devourer.reactions.ReactionAfter;
import org.googolplex.devourer.reactions.ReactionAt;
import org.googolplex.devourer.reactions.ReactionBefore;

/**
 * Date: 19.02.13
 * Time: 13:54
 */
public interface ReactionBindingBuilder {
    ReactionBindingBuilder doBefore(ReactionBefore reaction);
    ReactionBindingBuilder doAt(ReactionAt reaction);
    ReactionBindingBuilder doAfter(ReactionAfter reaction);
    ReactionBindingBuilder and();
}
