package org.googolplex.devourer.sandbox2;

import com.google.common.collect.ImmutableList;
import org.googolplex.devourer.configuration.modular.AbstractMappingModule;

/**
 * Date: 22.02.13
 * Time: 18:05
 */
public class PersonModule2 extends AbstractMappingModule {
    @Override
    protected void configure() {
        on("/persons")
            .doBefore((stacks, context) -> stacks.push(ImmutableList.builder()))
            .doAfter((stacks, context) -> {
                ImmutableList.Builder<Person> builder = stacks.pop();
                stacks.push(builder.build());
            });

        on("/persons/person")
            .doBefore((stacks, context) -> stacks.push("person", context.attribute("id").get()))
            .doAfter((stacks, context) -> {
                String name = stacks.pop("person");
                int id = Integer.parseInt(stacks.<String>pop("person"));
                ImmutableList.Builder<Login> loginsBuilder = stacks.pop("logins");
                ImmutableList.Builder<Person> personsBuilder = stacks.peek();
                personsBuilder.add(new Person(id, name, loginsBuilder.build()));
            });

        on("/persons/person/name")
            .doAt((stacks, context, body) -> stacks.push("person", body));

        on("/persons/person/logins")
            .doBefore((stacks, context) -> stacks.push("logins", ImmutableList.builder()))
            .doAfter((stacks, context) -> {
                stacks.push(
                    "logins",
                    stacks.<ImmutableList.Builder<Login>>pop("logins").build()
                );
            });

        on("/persons/person/logins/login")
            .doAt((stacks, context, body) -> {
                stacks.<ImmutableList.Builder<Login>>peek("logins")
                      .add(new Login(context.attribute("site").get(), body));
            });
    }
}

