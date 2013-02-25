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

import org.bitbucket.googolplex.devourer.sandbox2.Login;
import org.bitbucket.googolplex.devourer.sandbox2.Person;
import org.bitbucket.googolplex.devourer.sandbox2.PersonAnnotatedConfig;
import org.bitbucket.googolplex.devourer.sandbox2.PersonModule;
import org.bitbucket.googolplex.devourer.stacks.Stacks;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

        checkStacks(stacks);
    }

    @Test
    public void testAnnotations() throws Exception {
        Devourer devourer = Devourer.create(new PersonAnnotatedConfig());
        Stacks stacks = devourer.parse(EXAMPLE);

        checkStacks(stacks);
    }

    private void checkStacks(Stacks stacks) {
        List<Person> persons = stacks.pop();
        assertEquals(3, persons.size());

        Person person = persons.get(0);
        assertEquals(1, person.id);
        assertEquals("Foo Bar", person.name);
        assertEquals(2, person.logins.size());
        Login login = person.logins.get(0);
        assertEquals("example.com", login.site);
        assertEquals("foobar", login.value);
        login = person.logins.get(1);
        assertEquals("example.org", login.site);
        assertEquals("f.bar", login.value);

        person = persons.get(1);
        assertEquals(2, person.id);
        assertEquals("Baz Boo", person.name);
        assertEquals(1, person.logins.size());
        login = person.logins.get(0);
        assertEquals("uni.edu", login.site);
        assertEquals("boo.baz", login.value);

        person = persons.get(2);
        assertEquals(4, person.id);
        assertEquals("Fizz B. Jr", person.name);
        assertTrue(person.logins.isEmpty());
    }
}
