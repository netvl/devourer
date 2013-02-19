package org.googolplex.devourer.configuration.reactions;

import org.googolplex.devourer.Stacks;
import org.googolplex.devourer.contexts.AttributesContext;

/**
 * Date: 19.02.13
 * Time: 11:04
 */
public interface ReactionAt {
    void react(Stacks stacks, AttributesContext context, String body);
}
