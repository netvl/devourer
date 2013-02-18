package org.googolplex.devourer.sandbox;

import org.googolplex.devourer.Constants;

/**
 * Date: 18.02.13
 * Time: 19:06
 *
 * @author Vladimir Matveev
 */
public abstract class AbstractDevoConfig {
    protected final <T> void pushTo(String stack, T object) {
    }

    protected final <T> void push(T object) {
        pushTo(Constants.Stacks.MAIN_STACK, object);
    }

    protected final <T> T popFrom(String stack) {
        return null;
    }

    protected final <T> T pop() {
        return popFrom(Constants.Stacks.MAIN_STACK);
    }

    protected final <T> T peekFrom(String stack, int i) {
        return null;
    }

    protected final <T> T peek(int i) {
        return peekFrom(Constants.Stacks.MAIN_STACK, i);
    }

    protected final <T> T peekFrom(String stack) {
        return peekFrom(stack, 0);
    }

    protected final <T> T peek() {
        return peekFrom(Constants.Stacks.MAIN_STACK, 0);
    }
}
