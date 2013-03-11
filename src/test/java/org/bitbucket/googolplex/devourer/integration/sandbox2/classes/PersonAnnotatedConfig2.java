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

package org.bitbucket.googolplex.devourer.integration.sandbox2.classes;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.bitbucket.googolplex.devourer.configuration.annotated.annotations.*;
import org.bitbucket.googolplex.devourer.contexts.AttributesContext;

import java.util.List;

/**
 * Date: 24.02.13
 * Time: 9:12
 *
 * @author Vladimir Matveev
 */
@CustomNamespaceContext(
    {"urn:example", "e"}
)
public class PersonAnnotatedConfig2 {
    @Before("/persons")
    public ImmutableList.Builder<Person> beforePersons() {
        return ImmutableList.builder();
    }

    @Before("/persons/person")
    @PushTo("person")
    public String personId(AttributesContext context) {
        return context.attribute("id").get();
    }

    @At("/persons/person/name")
    @PushTo("person")
    public String personName(String body) {
        return body;
    }

    @Before("/persons/person/logins")
    @PushTo("logins")
    public ImmutableList.Builder<Login> beforeLogins() {
        return ImmutableList.builder();
    }

    @At("/persons/person/logins/login")
    public void addLogin(@PeekFrom("logins") ImmutableList.Builder<Login> loginsBuilder, String body,
                         AttributesContext context) {
        loginsBuilder.add(new Login(context.attribute("site").get(), body));
    }

    @After("/persons/person/logins")
    @PushTo("logins")
    public List<Login> afterLogins(@PopFrom("logins") ImmutableList.Builder<Login> loginsBuilder) {
        return loginsBuilder.build();
    }

    @After("/persons/person")
    public void afterPerson(@PopFrom("person") String name, @PopFrom("person") String id,
                            @PopFrom("logins") Optional<List<Login>> logins,
                            @Peek ImmutableList.Builder<Person> personsBuilder) {
        personsBuilder.add(new Person(Integer.parseInt(id), name, logins.or(ImmutableList.<Login>of())));
    }

    @After("/persons")
    public List<Person> afterPersons(@Pop ImmutableList.Builder<Person> personsBuilder) {
        return personsBuilder.build();
    }
}
