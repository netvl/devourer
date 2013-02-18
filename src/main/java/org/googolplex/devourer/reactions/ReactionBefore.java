package org.googolplex.devourer.reactions;

import org.googolplex.devourer.StackState;
import org.googolplex.devourer.contexts.AttributedContext;

/**
 * Date: 18.02.13
 * Time: 20:18
 *
 * @author Vladimir Matveev
 */
public interface ReactionBefore extends Reaction {
    void react(StackState stackState, AttributedContext context);
}
