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

package org.bitbucket.googolplex.devourer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bitbucket.googolplex.devourer.sandbox.ExampleAnnotatedConfig;
import org.bitbucket.googolplex.devourer.sandbox.ExampleData;
import org.bitbucket.googolplex.devourer.sandbox.ExampleDataModule;
import org.bitbucket.googolplex.devourer.sandbox.ExampleDataModuleNamespaces;
import org.bitbucket.googolplex.devourer.stacks.Stacks;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Date: 19.02.13
 * Time: 14:24
 */
public class DevourerTest {
    private static final String EXAMPLE =
        "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
        "<data>\n" +
        "  <datum id=\"34\">\n" +
        "    <name>Name</name>\n" +
        "    <arg>0.3</arg>\n" +
        "    <arg>0.2</arg>\n" +
        "    <header name=\"Header-1\">header 1 value</header>\n" +
        "    <header name=\"Header-2\">\n" +
        "      Some bigger value\n" +
        "    </header>\n" +
        "  </datum>\n" +
        "</data>";

    @Test
    public void testModularConfig() throws Exception {
        Devourer devourer = Devourer.create(new ExampleDataModule());
        Stacks stacks = devourer.parse(EXAMPLE);

        List<ExampleData> dataList = stacks.pop();
        assertEquals(1, dataList.size());

        ExampleData data = dataList.get(0);
        assertEquals(34, data.id);
        assertEquals("Name", data.name);
        assertEquals(ImmutableList.of(0.3, 0.2), data.args);
        assertEquals(ImmutableMap.of("Header-1", "header 1 value", "Header-2", "Some bigger value"), data.headers);
    }

    @Test
    public void testAnnotatedConfig() throws Exception {
        Devourer devourer = Devourer.create(new ExampleAnnotatedConfig());
        Stacks stacks = devourer.parse(EXAMPLE);

        List<ExampleData> dataList = stacks.get("results").pop();
        assertEquals(1, dataList.size());

        ExampleData data = dataList.get(0);
        assertEquals(34, data.id);
        assertEquals("Name", data.name);
        assertEquals(ImmutableList.of(0.3, 0.2), data.args);
        assertEquals(ImmutableMap.of("Header-1", "header 1 value", "Header-2", "Some bigger value"), data.headers);
    }

    private static final String EXAMPLE_2 =
        "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
        "<p:data xmlns:p=\"urn:example:namespace\" xmlns:d=\"urn:example:double\" xmlns:h=\"urn:example:header\">\n" +
        "  <p:datum id=\"34\">\n" +
        "    <p:name>Name</p:name>\n" +
        "    <d:name>Another name</d:name>\n" +
        "    <d:arg>0.3</d:arg>\n" +
        "    <d:arg>0.2</d:arg>\n" +
        "    <p:arg>0.1</p:arg>\n" +
        "    <p:arg>0.01</p:arg>\n" +
        "    <h:header name=\"Header-1\">header 1 value</h:header>\n" +
        "    <h:header name=\"Header-2\">\n" +
        "      Some bigger value\n" +
        "    </h:header>\n" +
        "  </p:datum>\n" +
        "</p:data>\n";

    @Test
    public void testNamespaces() throws Exception {
        Devourer devourer = Devourer.create(new ExampleDataModuleNamespaces());
        Stacks stacks = devourer.parse(EXAMPLE_2);

        List<ExampleData> dataList = stacks.pop();
        assertEquals(1, dataList.size());

        ExampleData data = dataList.get(0);
        assertEquals(34, data.id);
        assertEquals("Name", data.name);
        assertEquals(ImmutableList.of(0.3, 0.2), data.args);
        assertEquals(ImmutableMap.of("Header-1", "header 1 value", "Header-2", "Some bigger value"), data.headers);
    }

}
