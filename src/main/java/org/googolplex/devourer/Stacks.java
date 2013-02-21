/*******************************************************************************
 * Copyright 2013 Vladimir Matveev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.googolplex.devourer;

import java.util.NoSuchElementException;

/**
 * An interface to a set of named stacks. Also provides an access to some default stack.
 * A stack is created on the first write access to it.
 *
 * <p>The default implementation, {@link DefaultStacks}, is not thread-safe. It is not a problem for
 * Devourer itself (it does not use threads during parsing process), but should be taken into account
 * when you work with this object after parsing is done.</p>
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

    /**
     * Checks whether the given stack is empty.
     *
     * @param stack name of the stack
     * @return {@code true} if stack is empty or does not exist, {@code false} otherwise
     */
    boolean isEmpty(String stack);

    /**
     * Checks whether the default stack is empty.
     *
     * @return {@code true} if stack is empty or does not exist, {@code false} otherwise
     */
    boolean isEmpty();
}
