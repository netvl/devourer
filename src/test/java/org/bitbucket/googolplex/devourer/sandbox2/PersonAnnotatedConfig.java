package org.bitbucket.googolplex.devourer.sandbox2;

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
public class PersonAnnotatedConfig {
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
