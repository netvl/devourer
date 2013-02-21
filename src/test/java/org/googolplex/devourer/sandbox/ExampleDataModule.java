package org.googolplex.devourer.sandbox;

import com.google.common.collect.ImmutableList;
import org.googolplex.devourer.Stacks;
import org.googolplex.devourer.configuration.modular.AbstractMappingModule;
import org.googolplex.devourer.configuration.reactions.ReactionAfter;
import org.googolplex.devourer.configuration.reactions.ReactionAt;
import org.googolplex.devourer.configuration.reactions.ReactionBefore;
import org.googolplex.devourer.contexts.AttributesContext;

/**
 * Date: 19.02.13
 * Time: 10:11
 */
public class ExampleDataModule extends AbstractMappingModule {
    @Override
    public void configure() {
        on("/data")
            .doBefore(new ReactionBefore() {
                @Override
                public void react(Stacks stacks, AttributesContext context) {
                    stacks.push(ImmutableList.builder());
                }
            })
            .doAfter(new ReactionAfter() {
                @Override
                public void react(Stacks stacks, AttributesContext context) {
                    ImmutableList.Builder<ExampleData> dataBuilder = stacks.pop();
                    stacks.push(dataBuilder.build());
                }
            });

        on("/data/datum")
            .doBefore(new ReactionBefore() {
                @Override
                public void react(Stacks stacks, AttributesContext context) {
                    ExampleData.Builder builder = new ExampleData.Builder();
                    builder.setId(Integer.parseInt(context.attribute("id").or("0")));
                    stacks.push(builder);
                }
            })
            .doAfter(new ReactionAfter() {
                @Override
                public void react(Stacks stacks, AttributesContext context) {
                    ExampleData.Builder builder = stacks.pop();
                    ImmutableList.Builder<ExampleData> dataBuilder = stacks.peek();
                    dataBuilder.add(builder.build());
                }
            });

        on("/data/datum/name")
            .doAt(new ReactionAt() {
                @Override
                public void react(Stacks stacks, AttributesContext context, String body) {
                    ExampleData.Builder builder = stacks.peek();
                    builder.setName(body);
                }
            });

        on("/data/datum/arg")
            .doAt(new ReactionAt() {
                @Override
                public void react(Stacks stacks, AttributesContext context, String body) {
                    ExampleData.Builder builder = stacks.peek();
                    builder.addArg(Double.parseDouble(body));
                }
            });

        on("/data/datum/header")
            .doAt(new ReactionAt() {
                @Override
                public void react(Stacks stacks, AttributesContext context, String body) {
                    ExampleData.Builder builder = stacks.peek();
                    builder.addHeader(context.attribute("name").get(), body);
                }
            });

    }
}
