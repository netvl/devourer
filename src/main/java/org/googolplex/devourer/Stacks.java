package org.googolplex.devourer;

import java.util.NoSuchElementException;

/**
 * An interface to a set of named stacks. Also provides an access to some default stack.
 * A stack is created on the first write access to it.
 */
public interface Stacks {
    /**
     * A constant name of the default stack. {@code stacks.push(DEFAULT_STACK, object)} is
     * equivalent to {@code stacks.push(object)}; the same goes for {@code stacks.pop(...)}
     * and {@code stacks.peek(...)}.
     */
    public static final String DEFAULT_STACK = "main";

    /**
     * Pushes new element to the specified stack. The stack will be created if it is not present.
     *
     * @param stack name of the stack
     * @param object object to push onto the stack
     * @param <T> type of the object
     */
    <T> void push(String stack, T object);

    /**
     * Pushes new element to the default stack. The stack will be created if it is not present.
     *
     * @param object object to push onto the stack
     * @param <T> type of the object
     */
    <T> void push(T object);

    /**
     * Peeks an element from the top of the specified stack. The object will not be removed from the stack.
     *
     * @param stack name of the stack
     * @param <T> type of the object
     * @return an object from the top of the stack
     * @throws NoSuchElementException if stack is empty or does not exist
     */
    <T> T peek(String stack) throws NoSuchElementException;

    /**
     * Peeks an element from the top of the default stack. The object will not be removed from the stack.
     *
     * @param <T> type of the object
     * @return an object from the top of the stack
     * @throws NoSuchElementException if stack is empty or does not exist
     */
    <T> T peek() throws NoSuchElementException;

    /**
     * Pops an element from the top of the specified stack. The object will be removed from the stack.
     *
     * @param stack name of the stack
     * @param <T> type of the object
     * @return an object from the top of the stack
     * @throws NoSuchElementException if stack is empty or does not exist
     */
    <T> T pop(String stack) throws NoSuchElementException;

    /**
     * Pops an element from the top of the default stack. The object will be removed from the stack.
     *
     * @param <T> type of the object
     * @return an object from the top of the stack
     * @throws NoSuchElementException if stack is empty or does not exist
     */
    <T> T pop() throws NoSuchElementException;
}
