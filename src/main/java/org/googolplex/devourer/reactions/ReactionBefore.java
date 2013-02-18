package org.googolplex.devourer.reactions;

import org.googolplex.devourer.Stacks;
import org.googolplex.devourer.contexts.AttributesContext;

/**
 * Date: 18.02.13
 * Time: 20:18
 *
 * @author Vladimir Matveev
 */
public interface ReactionBefore extends Reaction {
    void react(Stacks stacks, AttributesContext context);
}
