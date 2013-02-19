package org.googolplex.devourer.sandbox;

import com.google.common.collect.ImmutableList;
import org.googolplex.devourer.Constants;
import org.googolplex.devourer.contexts.AttributesContext;

import java.util.List;

/**
 * Date: 18.02.13
 * Time: 15:57
 *
 * @author Vladimir Matveev
 */
public class DevoConfig1 {
    @Before("/data")
//    @PushTo(Constants.Stacks.MAIN_STACK)    // unnecessary
    public ImmutableList.Builder<ExampleData> initData() {
        return ImmutableList.builder();
    }

    @Before("/data/datum")
    public ExampleData.Builder createDatum(AttributesContext context) {
        ExampleData.Builder builder = new ExampleData.Builder();
        builder.setId(Integer.parseInt(context.attribute("id").or("0")));
        return builder;
    }

    @At("/data/datum/name")
    public void setDatumName(@Peek ExampleData.Builder builder, String body) {
        builder.setName(body);
    }

    @At("/data/datum/arg")
    public void addDatumArg(@Peek ExampleData.Builder builder, String body) {
        builder.addArg(Double.parseDouble(body));
    }

    @At("/data/datum/header")
    public void addDatumHeader(@Peek ExampleData.Builder builder, AttributesContext context, String body) {
        builder.addHeader(context.attribute("name").get(), body);
    }

    @After("/data/datum")
    public void addDatum(@Pop ExampleData.Builder builder, @Peek ImmutableList.Builder<ExampleData> dataBuilder) {
        dataBuilder.add(builder.build());
    }

    @After("/data")
    @PushTo(Constants.Stacks.RESULT_STACK)
    public List<ExampleData> finishData(@Pop ImmutableList.Builder<ExampleData> dataBuilder) {
        return dataBuilder.build();
    }
}
