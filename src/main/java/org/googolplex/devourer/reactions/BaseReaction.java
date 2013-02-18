package org.googolplex.devourer.reactions;

import org.googolplex.devourer.StackState;
import org.googolplex.devourer.contexts.AttributedContext;
import org.googolplex.devourer.contexts.ElementContext;

/**
 * Date: 18.02.13
 * Time: 14:58
 */
public abstract class BaseReaction {
    protected StackState stackState;

    public void setStackState(StackState stackState) {
        this.stackState = stackState;
    }

    public void start(AttributedContext context) {
    }

    public void body(ElementContext context, String body) {
    }

    public void end(ElementContext context) {
    }
}
