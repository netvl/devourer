package org.googolplex.devourer.reactions;

import org.googolplex.devourer.StackState;
import org.googolplex.devourer.contexts.AttributedContext;

/**
 * Date: 18.02.13
 * Time: 20:19
 *
 * @author Vladimir Matveev
 */
public interface ReactionAfter extends Reaction {
    void react(StackState stackState, AttributedContext context, String body);
}
