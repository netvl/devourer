package org.googolplex.devourer.sandbox2;

import java.util.List;

/**
 * Date: 22.02.13
 * Time: 17:49
 */
public class Person {
    public final int id;
    public final String name;
    public final List<Login> logins;

    public Person(int id, String name, List<Login> logins) {
        this.id = id;
        this.name = name;
        this.logins = logins;
    }
}
