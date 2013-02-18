package org.googolplex.devourer.sandbox;

import com.google.common.collect.ImmutableList;
import org.googolplex.devourer.Constants;
import org.googolplex.devourer.Stacks;
import org.googolplex.devourer.contexts.AttributesContext;
import org.googolplex.devourer.reactions.ReactionAfter;
import org.googolplex.devourer.reactions.ReactionMapping;
import org.googolplex.devourer.reactions.ReactionBefore;

import static org.googolplex.devourer.reactions.ReactionMapping.map;

/**
 * Date: 18.02.13
 * Time: 20:18
 *
 * @author Vladimir Matveev
 */
public class DevoConfig2 {
    public final ReactionMapping INIT_DATA =
        map("/data").to(new ReactionBefore() {
            @Override
            public void react(Stacks stacks, AttributesContext context) {
                stacks.push(ImmutableList.builder());
            }
        });

    public final ReactionMapping CREATE_DATUM =
        map("/data").to(new ReactionBefore() {
            @Override
            public void react(Stacks stacks, AttributesContext context) {
                ExampleData.Builder builder = new ExampleData.Builder();
                builder.setId(Integer.parseInt(context.attribute("id").or("0")));
                stacks.push(builder);
            }
        });

    public final ReactionMapping SET_DATUM_NAME =
        map("/data/datum/name").to(new ReactionAfter() {
            @Override
            public void react(Stacks stacks, AttributesContext context, String body) {
                ExampleData.Builder builder = stacks.peek();
                builder.setName(body);
            }
        });

    public final ReactionMapping ADD_DATUM_ARG =
        map("/data/datum/arg").to(new ReactionAfter() {
            @Override
            public void react(Stacks stacks, AttributesContext context, String body) {
                ExampleData.Builder builder = stacks.peek();
                builder.addArg(Double.parseDouble(body));
            }
        });

    public final ReactionMapping ADD_DATUM_HEADER =
        map("/data/datum/header").to(new ReactionAfter() {
            @Override
            public void react(Stacks stacks, AttributesContext context, String body) {
                ExampleData.Builder builder = stacks.peek();
                builder.addHeader(context.attribute("name").get(), body);
            }
        });

    public final ReactionMapping ADD_DATUM =
        map("/data/datum").to(new ReactionAfter() {
            @Override
            public void react(Stacks stacks, AttributesContext context, String body) {
                ExampleData.Builder builder = stacks.pop();
                ImmutableList.Builder<ExampleData> dataBuilder = stacks.peek();
                dataBuilder.add(builder.build());
            }
        });

    public final ReactionMapping FINISH_DATA =
        map("/data").to(new ReactionAfter() {
            @Override
            public void react(Stacks stacks, AttributesContext context, String body) {
                ImmutableList.Builder<ExampleData> dataBuilder = stacks.pop();
                stacks.push(Constants.Stacks.RESULT_STACK, dataBuilder.build());
            }
        });
}
