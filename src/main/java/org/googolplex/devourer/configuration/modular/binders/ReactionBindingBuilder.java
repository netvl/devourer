package org.googolplex.devourer.configuration.modular.binders;

import org.googolplex.devourer.reactions.ReactionAfter;
import org.googolplex.devourer.reactions.ReactionAt;
import org.googolplex.devourer.reactions.ReactionBefore;

/**
 * Date: 19.02.13
 * Time: 13:54
 */
public interface ReactionBindingBuilder {
    void to(ReactionBefore reaction);
    void to(ReactionAt reaction);
    void to(ReactionAfter reaction);
}
