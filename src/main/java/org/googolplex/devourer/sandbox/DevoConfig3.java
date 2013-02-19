package org.googolplex.devourer.sandbox;

import com.google.common.collect.ImmutableList;
import org.googolplex.devourer.Stacks;
import org.googolplex.devourer.configuration.modular.AbstractMappingModule;
import org.googolplex.devourer.contexts.AttributesContext;
import org.googolplex.devourer.reactions.ReactionAfter;
import org.googolplex.devourer.reactions.ReactionAt;
import org.googolplex.devourer.reactions.ReactionBefore;

/**
 * Date: 19.02.13
 * Time: 10:11
 */
public class DevoConfig3 extends AbstractMappingModule {
    public void configure() {
        map("/data").to(new ReactionBefore() {
            @Override
            public void react(Stacks stacks, AttributesContext context) {
                stacks.push(ImmutableList.builder());
            }
        });

        map("/data/datum").to(new ReactionBefore() {
            @Override
            public void react(Stacks stacks, AttributesContext context) {
                ExampleData.Builder builder = new ExampleData.Builder();
                builder.setId(Integer.parseInt(context.attribute("id").or("0")));
                stacks.push(builder);
            }
        });

        map("/data/datum/name").to(new ReactionAt() {
            @Override
            public void react(Stacks stacks, AttributesContext context, String body) {
                ExampleData.Builder builder = stacks.peek();
                builder.setName(body);
            }
        });

        map("/data/datum/arg").to(new ReactionAt() {
            @Override
            public void react(Stacks stacks, AttributesContext context, String body) {
                ExampleData.Builder builder = stacks.peek();
                builder.addArg(Double.parseDouble(body));
            }
        });

        map("/data/datum/header").to(new ReactionAt() {
            @Override
            public void react(Stacks stacks, AttributesContext context, String body) {
                ExampleData.Builder builder = stacks.peek();
                builder.addHeader(context.attribute("name").get(), body);
            }
        });

        map("/data/datum").to(new ReactionAfter() {
            @Override
            public void react(Stacks stacks, AttributesContext context) {
                ExampleData.Builder builder = stacks.pop();
                ImmutableList.Builder<ExampleData> dataBuilder = stacks.peek();
                dataBuilder.add(builder.build());
            }
        });

        map("/data").to(new ReactionAfter() {
            @Override
            public void react(Stacks stacks, AttributesContext context) {
                ImmutableList.Builder<ExampleData> dataBuilder = stacks.pop();
                stacks.push(dataBuilder.build());
            }
        });
    }
}
