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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

/**
 * Date: 18.02.13
 * Time: 19:09
 *
 * @author Vladimir Matveev
 */
public class ExampleData {
    public final int id;
    public final String name;
    public final List<Double> args;
    public final Map<String, String> headers;

    public ExampleData(int id, String name, List<Double> args, Map<String, String> headers) {
        Preconditions.checkNotNull(name, "Name is null");
        Preconditions.checkNotNull(args, "Args are null");
        Preconditions.checkNotNull(headers, "Headers are null");

        this.id = id;
        this.name = name;
        this.args = args;
        this.headers = headers;
    }

    public static class Builder {
        private int id;
        private String name;
        private ImmutableList.Builder<Double> argsBuilder = ImmutableList.builder();
        private ImmutableMap.Builder<String, String>  headersBuilder = ImmutableMap.builder();

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder addArg(double value) {
            this.argsBuilder.add(value);
            return this;
        }

        public Builder addHeader(String name, String value) {
            this.headersBuilder.put(name, value);
            return this;
        }

        public ExampleData build() {
            return new ExampleData(id, name, argsBuilder.build(), headersBuilder.build());
        }
    }

}
