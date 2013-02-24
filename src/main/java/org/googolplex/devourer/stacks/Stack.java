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

package org.googolplex.devourer.stacks;

import com.google.common.base.Optional;

import java.util.NoSuchElementException;

/**
 * Represents simple heterogenous stack. Instances of this interface are returned by {@link Stacks} object.
 */
public interface Stack {
    /**
     * Pushes new element to this stack.
     *
     * @param object object to add to the stack
     * @param <T> type of the object
     */
    <T> void push(T object);

    /**
     * Returns an element from the top of the stack. The element will not be removed from the stack.
     *
     * @param <T> type of the object
     * @return an object from the top of the stack
     * @throws NoSuchElementException if stack is empty
     */
    <T> T peek() throws NoSuchElementException;

    /**
     * Safe version of {@link #peek()}, returns an element from the top of the stack if stack is not empty;
     * absent value is returned otherwise. The element will not be removed from the stack.
     *
     * @param <T> type of the object
     * @return an object from the top of the stack, if it is present
     */
    <T> Optional<T> tryPeek();

    /**
     * Returns an element from the top of the stack. The element will be removed from the stack.
     *
     * @param <T> type of the object
     * @return an object from the top of the stack
     * @throws NoSuchElementException if stack is empty
     */
    <T> T pop() throws NoSuchElementException;

    /**
     * Safe version of {@link #pop()}, returns an element from the top of the stack if stack is not empty;
     * absent value is returned otherwise. The element will be removed from the stack.
     *
     * @param <T> type of the object
     * @return an object from the top of the stack, if it is present
     */
    <T> Optional<T> tryPop();

    /**
     * @return {@code true} if the stack is empty, {@code false} otherwise
     */
    boolean isEmpty();

    /**
     * @return number of elements in the stack
     */
    int size();
}
