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

package org.bitbucket.googolplex.devourer.configuration.modular;

import com.google.common.base.Preconditions;
import org.bitbucket.googolplex.devourer.configuration.modular.binders.ActionBindingBuilder;
import org.bitbucket.googolplex.devourer.configuration.modular.binders.MappingBinder;
import org.bitbucket.googolplex.devourer.configuration.modular.binders.NamespaceContextBuilder;

/**
 * Convenient implementation of {@link MappingModule} which provides an access to {@link MappingBinder} methods
 * directly from the body of {@link #configure()} method.
 *
 * <p>DSL usage looks like this:</p>
 * <pre>
 *     public class ExampleModule extends AbstractMappingModule {
 *        {@literal @}Override
 *         public void configure() {
 *             namespaceContext()
 *                 .map("urn:namespace").to("n")
 *                 .map("http://some-where.com/schema.xsd").to("s");
 *             on("/")
 *                 .doBefore(new ActionBefore() {
 *                    {@literal @}Override
 *                     public void act(Stacks stacks, AttributesContext context) {
 *                         stacks.push(ImmutableMap.builder());
 *                     }
 *                 })
 *                 .doAfter(new ActionAfter() {
 *                    {@literal @}Override
 *                     public void act(Stacks stacks, AttributesContext context) {
 *                         ImmutableMap.Builder<String, String> builder = stacks.pop();
 *                         stacks.push(builder.build());
 *                     }
 *                 });
 *
 *             on("/header")
 *                 .doBefore(new ActionBefore() {
 *                    {@literal @}Override
 *                     public void act(Stacks stacks, AttributesContext context) {
 *                         stacks.push(context.attribute("name").get());
 *                     }
 *                 })
 *                 .doAt(new ActionAt() {
 *                    {@literal @}Override
 *                     public void act(Stacks stacks, AttributesContext context, String body) {
 *                         stacks.push(body);
 *                     }
 *                 })
 *                 .doAfter(new ActionAfter() {
 *                    {@literal @}Override
 *                     public void act(Stacks stacks, AttributesContext context) {
 *                         String value = stacks.pop();
 *                         String name = stacks.pop();
 *                         ImmutableMap.Builder<String, String> builder = stacks.peek();
 *                         builder.put(name, value);
 *                     }
 *                 });
 *             }
 *         }
 *     }
 * </pre>
 *
 * <p>That is, a configuration consists of series of {@code on()} clauses. Each clause defines a set of actions
 * which should be done either before, at or after corresponding XML node. Before-actions are invoked when
 * the start element is reached. At-actions are invoked when content of the element is reached. After-actions
 * are invoked when the end element is reached. It is possible to have several {@code on()} clauses; this has
 * the same effect as if you have written all action mappings sequentially inside one clause, i.e.
 * <pre>
 *     on("/node")
 *         .doBefore(action1)
 *         .doAt(action2)
 *         .doAfter(action3);
 *
 *     on("/node")
 *         .doBefore(action4)
 *         .doAt(action5)
 *         .doAfter(action6);
 * </pre>
 * is equivalent to
 * <pre>
 *     on("/node")
 *         .doBefore(action1)
 *         .doAt(action2)
 *         .doAfter(action3)
 *         .doBefore(action4)
 *         .doAt(action5)
 *         .doAfter(action6);
 * </pre>
 * or
 * <pre>
 *     on("/node")
 *         .doBefore(action1)
 *         .doBefore(action4)
 *         .doAt(action2)
 *         .doAt(action5)
 *         .doAfter(action3)
 *         .doAfter(action6);
 * </pre>
 * or even
 * <pre>
 *     on("/node")
 *         .doAfter(action3)
 *         .doAfter(action6)
 *         .doBefore(action1)
 *         .doBefore(action4)
 *         .doAt(action2)
 *         .doAt(action5);
 * </pre>,
 * because the ordering between different types of actions does not matter. However, ordering
 * between the actions of the same type <i>does</i> matter: it is guaranteed that Devourer will execute these
 * actions exactly in this order when it reaches corresponding node.</p>
 *
 * <p>Note that at-actions are not invoked when the body is empty, that is, consists of whitespaces
 * only. Whether the body consists of whitespaces is determined by
 * {@link javax.xml.stream.XMLStreamReader#isWhiteSpace()} method. This behavior should be taken into account.</p>
 *
 * <p>Usually IDEs allow to fold anonymous class definitions. With this feature definitions of actions
 * looks like Java 8 lambda constructions. In fact, since all {@code Action*} interfaces are
 * <i>functional interfaces</i> in terms of Java 8, it is possible to use short lambda syntax to define actions
 * if you use Java 8. This makes mapping definitions look nice and clean while retaining good performance
 * characteristics.</p>
 *
 * <p>Instances of {@code Action*} interfaces are shared between threads when the Devourer is used from multiple
 * threads, so it is inadvisable to hold any state inside these objects directly, as it ruins Devourer's thread safety.
 * Use only the {@link org.bitbucket.googolplex.devourer.stacks.Stacks} object provided to the actions to hold state of the
 * parsing process; in this case thread safety is guaranteed.</p>
 */
public abstract class AbstractMappingModule implements MappingModule {
    private MappingBinder binder = null;

    protected final ActionBindingBuilder on(String route) {
        return binder.on(route);
    }

    protected final NamespaceContextBuilder namespaceContext() {
        return binder.namespaceContext();
    }

    @Override
    public synchronized final void configure(MappingBinder binder) {
        Preconditions.checkState(this.binder == null, "Cannot re-enter configuration process");
        Preconditions.checkNotNull(binder, "Binder is null");

        this.binder = binder;
        try {
            configure();
        } finally {
            this.binder = null;
        }
    }

    /**
     * This method should configure mappings for the Devourer.
     */
    protected abstract void configure();
}
