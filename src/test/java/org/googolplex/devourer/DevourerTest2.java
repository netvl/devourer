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

package org.googolplex.devourer;

import org.googolplex.devourer.sandbox2.PersonModule;
import org.googolplex.devourer.stacks.Stacks;
import org.junit.Test;

/**
 * Date: 24.02.13
 * Time: 9:24
 *
 * @author Vladimir Matveev
 */
public class DevourerTest2 {
    private static final String EXAMPLE =
        "<persons>\n" +
        "  <person id=\"1\">\n" +
        "    <name>Foo Bar</name>\n" +
        "    <logins>\n" +
        "      <login site=\"example.com\">foobar</login>\n" +
        "      <login site=\"example.org\">f.bar</login>\n" +
        "    </logins>\n" +
        "  </person>\n" +
        "  <person id=\"2\">\n" +
        "    <name>Baz Boo</name>\n" +
        "    <logins>\n" +
        "      <login site=\"uni.edu\">boo.baz</login>\n" +
        "    </logins>\n" +
        "  </person>\n" +
        "  <person id=\"4\">\n" +
        "    <name>Fizz B. Jr</name>\n" +
        "  </person>\n" +
        "</persons>\n";

    @Test
    public void testModule() throws Exception {
        Devourer devourer = Devourer.create(new PersonModule());

        Stacks stacks = devourer.parse(EXAMPLE);

    }
}
