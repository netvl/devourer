package org.googolplex.devourer.sandbox;

import com.google.common.collect.ImmutableList;
import org.googolplex.devourer.Constants;
import org.googolplex.devourer.StackState;
import org.googolplex.devourer.contexts.AttributedContext;
import org.googolplex.devourer.reactions.ReactionAfter;
import org.googolplex.devourer.reactions.ReactionMapping;
import org.googolplex.devourer.reactions.ReactionBefore;
import org.googolplex.devourer.reactions.ReactionMapping;

import static org.googolplex.devourer.reactions.ReactionMapping.map;

/**
 * Date: 18.02.13
 * Time: 20:18
 *
 * @author Vladimir Matveev
 */
public class DevoConfig2 {
    public final ReactionMapping INIT_DATA = map("/data").to(new ReactionBefore() {
        @Override
        public void react(StackState stackState, AttributedContext context) {
            stackState.push(ImmutableList.builder());
        }
    });

    public final ReactionMapping CREATE_DATUM = map("/data").to(new ReactionBefore() {
        @Override
        public void react(StackState stackState, AttributedContext context) {
            ExampleData.Builder builder = new ExampleData.Builder();
            builder.setId(Integer.parseInt(context.attribute("id").or("0")));
            stackState.push(builder);
        }
    });

    public final ReactionMapping SET_DATUM_NAME = map("/data/datum/name").to(new ReactionAfter() {
        @Override
        public void react(StackState stackState, AttributedContext context, String body) {
            ExampleData.Builder builder = stackState.peek();
            builder.setName(body);
        }
    });

    public final ReactionMapping ADD_DATUM_ARG = map("/data/datum/arg").to(new ReactionAfter() {
        @Override
        public void react(StackState stackState, AttributedContext context, String body) {
            ExampleData.Builder builder = stackState.peek();
            builder.addArg(Double.parseDouble(body));
        }
    });

    public final ReactionMapping ADD_DATUM_HEADER = map("/data/datum/header").to(new ReactionAfter() {
        @Override
        public void react(StackState stackState, AttributedContext context, String body) {
            ExampleData.Builder builder = stackState.peek();
            builder.addHeader(context.attribute("name").get(), body);
        }
    });

    public final ReactionMapping ADD_DATUM = map("/data/datum").to(new ReactionAfter() {
        @Override
        public void react(StackState stackState, AttributedContext context, String body) {
            ExampleData.Builder builder = stackState.pop();
            ImmutableList.Builder<ExampleData> dataBuilder = stackState.peek();
            dataBuilder.add(builder.build());
        }
    });

    public final ReactionMapping FINISH_DATA = map("/data").to(new ReactionAfter() {
        @Override
        public void react(StackState stackState, AttributedContext context, String body) {
            ImmutableList.Builder<ExampleData> dataBuilder = stackState.pop();
            stackState.push(Constants.Stacks.RESULT_STACK, dataBuilder.build());
        }
    });
}
