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

package org.bitbucket.googolplex.devourer.integration.sandbox;

import com.google.common.collect.ImmutableList;
import org.bitbucket.googolplex.devourer.stacks.Stacks;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAt;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionBefore;
import org.bitbucket.googolplex.devourer.configuration.modular.AbstractMappingModule;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAfter;
import org.bitbucket.googolplex.devourer.contexts.AttributesContext;

/**
 * Date: 19.02.13
 * Time: 10:11
 */
public class ExampleDataModule extends AbstractMappingModule {
    @Override
    public void configure() {
        on("/data")
            .doBefore(new ActionBefore() {
                @Override
                public void act(Stacks stacks, AttributesContext context) {
                    stacks.push(ImmutableList.builder());
                }
            })
            .doAfter(new ActionAfter() {
                @Override
                public void act(Stacks stacks, AttributesContext context) {
                    ImmutableList.Builder<ExampleData> dataBuilder = stacks.pop();
                    stacks.push(dataBuilder.build());
                }
            });

        on("/data/datum")
            .doBefore(new ActionBefore() {
                @Override
                public void act(Stacks stacks, AttributesContext context) {
                    ExampleData.Builder builder = new ExampleData.Builder();
                    builder.setId(Integer.parseInt(context.attribute("id").or("0")));
                    stacks.push(builder);
                }
            })
            .doAfter(new ActionAfter() {
                @Override
                public void act(Stacks stacks, AttributesContext context) {
                    ExampleData.Builder builder = stacks.pop();
                    ImmutableList.Builder<ExampleData> dataBuilder = stacks.peek();
                    dataBuilder.add(builder.build());
                }
            });

        on("/data/datum/name")
            .doAt(new ActionAt() {
                @Override
                public void act(Stacks stacks, AttributesContext context, String body) {
                    ExampleData.Builder builder = stacks.peek();
                    builder.setName(body);
                }
            });

        on("/data/datum/arg")
            .doAt(new ActionAt() {
                @Override
                public void act(Stacks stacks, AttributesContext context, String body) {
                    ExampleData.Builder builder = stacks.peek();
                    builder.addArg(Double.parseDouble(body));
                }
            });

        on("/data/datum/header")
            .doAt(new ActionAt() {
                @Override
                public void act(Stacks stacks, AttributesContext context, String body) {
                    ExampleData.Builder builder = stacks.peek();
                    builder.addHeader(context.attribute("name").get(), body);
                }
            });

    }
}
