package org.googolplex.devourer;

/**
 * Date: 18.02.13
 * Time: 15:11
 */
public interface StackState {
    <T> void push(String stack, T object);
    <T> void push(T object);
    <T> T peek(String stack);
    <T> T peek();
    <T> T pop(String stack);
    <T> T pop();
}
