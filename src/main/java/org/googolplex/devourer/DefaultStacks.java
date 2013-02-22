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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link Stacks} interface based on {@link HashMap} of {@link Deque}s.
 */
public class DefaultStacks implements Stacks {
    private final Map<String, Deque<Object>> stacks = new HashMap<String, Deque<Object>>();

    @Override
    public <T> void push(String stack, T object) {
        getStack(stack).push(object);
    }

    @Override
    public <T> void push(T object) {
        push(Stacks.DEFAULT_STACK, object);
    }

    @Override
    public <T> T peek(String stack) {
        return (T) getStack(stack).peek();
    }

    @Override
    public <T> T peek() {
        return peek(Stacks.DEFAULT_STACK);
    }

    @Override
    public <T> T pop(String stack) {
        return (T) getStack(stack).pop();
    }

    @Override
    public <T> T pop() {
        return pop(Stacks.DEFAULT_STACK);
    }

    @Override
    public boolean isEmpty(String stack) {
        return getStack(stack).isEmpty();
    }

    @Override
    public boolean isEmpty() {
        return isEmpty(Stacks.DEFAULT_STACK);
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
