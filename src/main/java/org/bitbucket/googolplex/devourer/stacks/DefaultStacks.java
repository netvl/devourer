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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import java.util.*;

/**
 * Default implementation of {@link Stacks} interface based on {@link HashMap} of {@link Deque} wrappers.
 *
 * <p>This implementation is not thread-safe.</p>
 */
public class DefaultStacks implements Stacks {
    private final Map<String, Stack> stacks = Maps.newHashMap();

    @Override
    public Stack get(String name) {
        if (stacks.containsKey(name)) {
            return stacks.get(name);
        } else {
            DequeWrapper stack = new DequeWrapper();
            stacks.put(name, stack);
            return stack;
        }
    }

    @Override
    public <T> Stack push(T object) {
        get(DEFAULT_STACK).push(object);
        return this;
    }

    @Override
    public <T> T peek() {
        return get(DEFAULT_STACK).peek();
    }

    @Override
    public <T> Optional<T> tryPeek() {
        return get(DEFAULT_STACK).tryPeek();
    }

    @Override
    public <T> T pop() {
        return get(DEFAULT_STACK).pop();
    }

    @Override
    public <T> Optional<T> tryPop() {
        return get(DEFAULT_STACK).tryPop();
    }

    @Override
    public boolean isEmpty() {
        return get(DEFAULT_STACK).isEmpty();
    }

    @Override
    public int size() {
        return get(DEFAULT_STACK).size();
    }

    @Override
    public <T> List<T> popList() {
        return get(DEFAULT_STACK).popList();
    }

    @Override
    public <T> List<T> peekList() {
        return get(DEFAULT_STACK).peekList();
    }

    private static class DequeWrapper implements Stack {
        private final Deque<Object> deque = new ArrayDeque<Object>();

        @Override
        public <T> Stack push(T object) {
            deque.push(object);
            return this;
        }

        @Override
        public <T> T peek() throws NoSuchElementException {
            return (T) deque.peek();
        }

        @Override
        public <T> Optional<T> tryPeek() {
            if (deque.isEmpty()) {
                return Optional.absent();
            } else {
                return Optional.of((T) deque.peek());
            }
        }

        @Override
        public <T> T pop() throws NoSuchElementException {
            return (T) deque.pop();
        }

        @Override
        public <T> Optional<T> tryPop() {
            if (deque.isEmpty()) {
                return Optional.absent();
            } else {
                return Optional.of((T) deque.pop());
            }
        }

        @Override
        public boolean isEmpty() {
            return deque.isEmpty();
        }

        @Override
        public int size() {
            return deque.size();
        }

        @Override
        public <T> List<T> popList() {
            List<T> result = (List<T>) ImmutableList.copyOf(deque).reverse();
            deque.clear();
            return result;
        }

        @Override
        public <T> List<T> peekList() {
            return (List<T>) ImmutableList.copyOf(deque).reverse();
        }
    }
}
