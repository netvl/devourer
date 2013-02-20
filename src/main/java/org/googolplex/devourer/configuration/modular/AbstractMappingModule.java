package org.googolplex.devourer.configuration.modular;

import com.google.common.base.Preconditions;
import org.googolplex.devourer.configuration.modular.binders.ReactionBindingBuilder;
import org.googolplex.devourer.configuration.modular.binders.MappingBinder;

/**
 * Convenient implementation of {@link MappingModule} which provides an access to {@link MappingBinder} methods
 * directly from the body of {@link #configure()} method.
 *
 * <p>DSL usage looks like this:</p>
 * <pre>
 *     public class ExampleModule extends AbstractMappingModule {
 *        {@literal @}Override
 *         public void configure() {
 *             on("/")
 *                 .doBefore(new ReactionBefore() {
 *                    {@literal @}Override
 *                     public void react(Stacks stacks, AttributesContext context) {
 *                         stacks.push(ImmutableMap.builder());
 *                     }
 *                 })
 *                 .doAfter(new ReactionAfter() {
 *                    {@literal @}Override
 *                     public void react(Stacks stacks, AttributesContext context) {
 *                         ImmutableMap.Builder<String, String> builder = stacks.pop();
 *                         stacks.push(builder.build());
 *                     }
 *                 });
 *
 *             on("/header")
 *                 .doBefore(new ReactionBefore() {
 *                    {@literal @}Override
 *                     public void react(Stacks stacks, AttributesContext context) {
 *                         stacks.push(context.attribute("name").get());
 *                     }
 *                 })
 *                 .doAt(new ReactionAt() {
 *                    {@literal @}Override
 *                     public void react(Stacks stacks, AttributesContext context, String body) {
 *                         stacks.push(body);
 *                     }
 *                 })
 *                 .doAfter(new ReactionAfter() {
 *                    {@literal @}Override
 *                     public void react(Stacks stacks, AttributesContext context) {
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
 * are invoked when the end element is reached.</p>
 *
 * <p>Note, however, that at-actions are not invoked when the body is empty, that is, consists of whitespaces
 * only. Whether the body consists of whitespaces is determined by
 * {@link javax.xml.stream.XMLStreamReader#isWhiteSpace()} method. This behavior should be taken into account.</p>
 *
 * <p>Usually IDEs allow to fold anonymous class definitions. With this feature definitions of reactions
 * looks like Java 8 lambda constructions. In fact, since all {@code Reaction*} interfaces are
 * <i>functional interfaces</i> in terms of Java 8, it is possible to use short lambda syntax to define reactions
 * if you use Java 8.</p>
 */
public abstract class AbstractMappingModule implements MappingModule {
    private MappingBinder binder = null;

    protected final ReactionBindingBuilder on(String route) {
        return binder.on(route);
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
