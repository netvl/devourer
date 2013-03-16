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

package org.bitbucket.googolplex.devourer.stacks;

import com.google.common.base.Optional;

import java.util.NoSuchElementException;

/**
 * An collection of stacks identified by name. Being also an extension of {@link Stack} interface,
 * provides access to some default stack.
 *
 * <p>The default implementation, {@link DefaultStacks}, is not thread-safe. It is not a problem for
 * Devourer itself (it does not use threads during parsing process), but should be taken into account
 * when you work with this object after parsing is done.</p>
 */
public interface Stacks extends Stack {
    /**
     * A constant name of the default stack. {@code stacks.push(DEFAULT_STACK, object)} is
     * equivalent to {@code stacks.push(object)}; the same goes for {@code stacks.pop(...)}
     * and {@code stacks.peek(...)}.
     */
    public static final String DEFAULT_STACK = "main";

    /**
     * Returns {@link Stack} instance for the stack with given name. The stack will be created if needed.
     *
     * @param name name of the stack
     * @return named stack
     */
    Stack get(String name);

    /**
     * Pushes new element to the default stack.
     *
     * @param object object to push onto the stack
     * @param <T> type of the object
     * @return this object
     */
    <T> Stack push(T object);

    /**
     * Peeks an element from the top of the default stack. The object will not be removed from the stack.
     *
     * @param <T> type of the object
     * @return an object from the top of the stack
     * @throws NoSuchElementException if stack is empty or does not exist
     */
    <T> T peek() throws NoSuchElementException;

    /**
     * Safe version of {@link #peek()}, returns an element from the top of the default stack if it is not empty,
     * absent value otherwise. The element will not be removed from the stack.
     *
     * @param <T> type of the object
     * @return an object from the top of the stack, if it is present
     */
    <T> Optional<T> tryPeek();

    /**
     * Pops an element from the top of the default stack. The object will be removed from the stack.
     *
     * @param <T> type of the object
     * @return an object from the top of the stack
     * @throws NoSuchElementException if stack is empty or does not exist
     */
    <T> T pop() throws NoSuchElementException;

    /**
     * Safe version of {@link #pop()}, returns an element from the top of the default stack if it is not empty,
     * absent value otherwise. The element will be removed from the stack.
     *
     * @param <T> type of the object
     * @return an object from the top of the stack, if it is present
     */
    <T> Optional<T> tryPop();

    /**
     * Checks whether the default stack is empty.
     *
     * @return {@code true} if stack is empty or does not exist, {@code false} otherwise
     */
    boolean isEmpty();

    /**
     * @return number of elements in the default stack
     */
    int size();
}
