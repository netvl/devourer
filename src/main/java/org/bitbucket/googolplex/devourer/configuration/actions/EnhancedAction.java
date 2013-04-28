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

package org.bitbucket.googolplex.devourer.configuration.actions;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import org.bitbucket.googolplex.devourer.contexts.ElementContext;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedName;
import org.bitbucket.googolplex.devourer.stacks.Stack;
import org.bitbucket.googolplex.devourer.stacks.Stacks;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Date: 27.04.13
 * Time: 20:33
 *
 * @author Vladimir Matveev
 */
public abstract class EnhancedAction implements ActionBefore, ActionAt, ActionAfter, ElementContext, Stacks {
    private final ThreadLocal<State> state = new ThreadLocal<State>();

    public abstract void act();

    @Override
    public void act(Stacks stacks, ElementContext context, String body) {
        state.set(new State(stacks, context, body));
        act();
    }

    @Override
    public void act(Stacks stacks, ElementContext context) {
        state.set(new State(stacks, context));
        act();
    }

    @Override
    public javax.xml.namespace.NamespaceContext realNamespaceContext() {
        return state.get().context.realNamespaceContext();
    }

    @Override
    public NamespaceContext customNamespaceContext() {
        return state.get().context.customNamespaceContext();
    }

    @Override
    public Collection<QualifiedName> attributeNames() {
        return state.get().context.attributeNames();
    }

    @Override
    public Optional<String> attribute(String localName) {
        return state.get().context.attribute(localName);
    }

    @Override
    public Optional<String> attribute(String localName, String namespace) {
        return state.get().context.attribute(localName, namespace);
    }

    @Override
    public Optional<String> attributeWithPrefix(String localName, String prefix) {
        return state.get().context.attributeWithPrefix(localName, prefix);
    }

    @Override
    public Optional<String> attribute(QualifiedName name) {
        return state.get().context.attribute(name);
    }

    @Override
    public QualifiedName elementName() {
        return state.get().context.elementName();
    }

    @Override
    public Stack get(String name) {
        return state.get().stacks.get(name);
    }

    @Override
    public <T> Stack push(T object) {
        return state.get().stacks.push(object);
    }

    @Override
    public <T> T peek() throws NoSuchElementException {
        return state.get().stacks.peek();
    }

    @Override
    public <T> Optional<T> tryPeek() {
        return state.get().stacks.tryPeek();
    }

    @Override
    public <T> T pop() throws NoSuchElementException {
        return state.get().stacks.pop();
    }

    @Override
    public <T> Optional<T> tryPop() {
        return state.get().stacks.tryPop();
    }

    @Override
    public boolean isEmpty() {
        return state.get().stacks.isEmpty();
    }

    @Override
    public int size() {
        return state.get().stacks.size();
    }

    @Override
    public <T> List<T> popList() {
        return state.get().stacks.popList();
    }

    @Override
    public <T> List<T> peekList() {
        return state.get().stacks.peekList();
    }

    protected String body() {
        return state.get().body.or(new Supplier<String>() {
            @Override
            public String get() {
                throw new IllegalStateException("Tried to access body from inside of invalid action type");
            }
        });
    }

    private static class State {
        private final Stacks stacks;
        private final ElementContext context;
        private final Optional<String> body;

        private State(Stacks stacks, ElementContext context, String body) {
            this.context = context;
            this.stacks = stacks;
            this.body = Optional.of(body);
        }

        private State(Stacks stacks, ElementContext context) {
            this.context = context;
            this.stacks = stacks;
            this.body = Optional.absent();
        }
    }
}
