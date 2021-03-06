package org.bitbucket.googolplex.devourer.integration.sandbox2.classes;

import com.google.common.collect.ImmutableList;
import org.bitbucket.googolplex.devourer.contexts.ElementContext;
import org.bitbucket.googolplex.devourer.stacks.Stacks;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAfter;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAt;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionBefore;
import org.bitbucket.googolplex.devourer.configuration.modular.AbstractMappingModule;

import java.util.List;

/**
 * Date: 22.02.13
 * Time: 18:05
 */
public class PersonModule2 extends AbstractMappingModule {
    @Override
    protected void configure() {
        namespaceContext()
            .map("urn:example").to("e");

        on("/e:persons")
            .doBefore(new ActionBefore() {
                @Override
                public void act(Stacks stacks, ElementContext context) {
                    stacks.push(ImmutableList.builder());
                }
            })
            .doAfter(new ActionAfter() {
                @Override
                public void act(Stacks stacks, ElementContext context) {
                    ImmutableList.Builder<Person> builder = stacks.pop();
                    stacks.push(builder.build());
                }
            });

        on("/e:persons/e:person")
            .doBefore(new ActionBefore() {
                @Override
                public void act(Stacks stacks, ElementContext context) {
                    stacks.get("person").push(context.attribute("id").get());
                }
            })
            .doAfter(new ActionAfter() {
                @Override
                public void act(Stacks stacks, ElementContext context) {
                    String name = stacks.get("person").pop();
                    int id = Integer.parseInt(stacks.get("person").<String>pop());
                    List<Login> logins = stacks.get("logins").<List<Login>>tryPop().or(ImmutableList.<Login>of());

                    ImmutableList.Builder<Person> personsBuilder = stacks.peek();
                    personsBuilder.add(new Person(id, name, logins));
                }
            });

        on("/e:persons/e:person/e:name")
            .doAt(new ActionAt() {
                @Override
                public void act(Stacks stacks, ElementContext context, String body) {
                    stacks.get("person").push(body);
                }
            });

        on("/e:persons/e:person/e:logins")
            .doBefore(new ActionBefore() {
                @Override
                public void act(Stacks stacks, ElementContext context) {
                    stacks.get("logins").push(ImmutableList.builder());
                }
            })
            .doAfter(new ActionAfter() {
                @Override
                public void act(Stacks stacks, ElementContext context) {
                    stacks.get("logins").push(
                        stacks.get("logins").<ImmutableList.Builder<Login>>pop().build()
                    );
                }
            });

        on("/e:persons/e:person/e:logins/e:login")
            .doAt(new ActionAt() {
                @Override
                public void act(Stacks stacks, ElementContext context, String body) {
                    stacks.get("logins").<ImmutableList.Builder<Login>>peek()
                          .add(new Login(context.attribute("site").get(), body));
                }
            });
    }
}

