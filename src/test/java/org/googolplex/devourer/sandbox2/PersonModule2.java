package org.googolplex.devourer.sandbox2;

import com.google.common.collect.ImmutableList;
import org.googolplex.devourer.Stacks;
import org.googolplex.devourer.configuration.actions.ActionAfter;
import org.googolplex.devourer.configuration.actions.ActionAt;
import org.googolplex.devourer.configuration.actions.ActionBefore;
import org.googolplex.devourer.configuration.modular.AbstractMappingModule;
import org.googolplex.devourer.contexts.AttributesContext;

/**
 * Date: 22.02.13
 * Time: 18:05
 */
public class PersonModule2 extends AbstractMappingModule {
    @Override
    protected void configure() {
        on("/persons")
            .doBefore(new ActionBefore() {
                @Override
                public void act(Stacks stacks, AttributesContext context) {
                    stacks.push(ImmutableList.builder());
                }
            })
            .doAfter(new ActionAfter() {
                @Override
                public void act(Stacks stacks, AttributesContext context) {
                    ImmutableList.Builder<Person> builder = stacks.pop();
                    stacks.push(builder.build());
                }
            });

        on("/persons/person")
            .doBefore(new ActionBefore() {
                @Override
                public void act(Stacks stacks, AttributesContext context) {
                    stacks.push("person", context.attribute("id").get());
                }
            })
            .doAfter(new ActionAfter() {
                @Override
                public void act(Stacks stacks, AttributesContext context) {
                    String name = stacks.pop("person");
                    int id = Integer.parseInt(stacks.<String>pop("person"));
                    ImmutableList.Builder<Login> loginsBuilder = stacks.pop("logins");
                    ImmutableList.Builder<Person> personsBuilder = stacks.peek();
                    personsBuilder.add(new Person(id, name, loginsBuilder.build()));
                }
            });

        on("/persons/person/name")
            .doAt(new ActionAt() {
                @Override
                public void act(Stacks stacks, AttributesContext context, String body) {
                    stacks.push("person", body);
                }
            });

        on("/persons/person/logins")
            .doBefore(new ActionBefore() {
                @Override
                public void act(Stacks stacks, AttributesContext context) {
                    stacks.push("logins", ImmutableList.builder());
                }
            })
            .doAfter(new ActionAfter() {
                @Override
                public void act(Stacks stacks, AttributesContext context) {
                    stacks.push(
                        "logins",
                        stacks.<ImmutableList.Builder<Login>>pop("logins").build()
                    );
                }
            });

        on("/persons/person/logins/login")
            .doAt(new ActionAt() {
                @Override
                public void act(Stacks stacks, AttributesContext context, String body) {
                    stacks.<ImmutableList.Builder<Login>>peek("logins")
                        .add(new Login(context.attribute("site").get(), body));
                }
            });
    }
}

