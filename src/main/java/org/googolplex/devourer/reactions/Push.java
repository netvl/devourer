package org.googolplex.devourer.reactions;

import org.googolplex.devourer.Constants;
import org.googolplex.devourer.contexts.AttributedContext;

/**
 * Date: 18.02.13
 * Time: 14:54
 */
public class Push<T> extends BaseReaction {
    private final String stack;
    private final T object;

    public Push(String stack, T object) {
        this.stack = stack;
        this.object = object;
    }

    public Push(T object) {
        this(Constants.Stacks.MAIN_STACK, object);
    }

    @Override
    public void start(AttributedContext context) {
        stackState.push(stack, object);
    }
}
