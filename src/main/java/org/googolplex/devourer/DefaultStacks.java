package org.googolplex.devourer;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 19.02.13
 * Time: 12:47
 */
public class DefaultStacks implements Stacks {
    private final Map<String, Deque<Object>> stacks = new HashMap<String, Deque<Object>>();

    @Override
    public <T> void push(String stack, T object) {
        getStack(stack).push(object);
    }

    @Override
    public <T> void push(T object) {
        push(Constants.Stacks.MAIN_STACK, object);
    }

    @Override
    public <T> T peek(String stack) {
        return (T) getStack(stack).peek();
    }

    @Override
    public <T> T peek() {
        return peek(Constants.Stacks.MAIN_STACK);
    }

    @Override
    public <T> T pop(String stack) {
        return (T) getStack(stack).pop();
    }

    @Override
    public <T> T pop() {
        return pop(Constants.Stacks.MAIN_STACK);
    }

    private Deque<Object> getStack(String name) {
        if (stacks.containsKey(name)) {
            return stacks.get(name);
        } else {
            Deque<Object> stack = new ArrayDeque<Object>();
            stacks.put(name, stack);
            return stack;
        }
    }
}