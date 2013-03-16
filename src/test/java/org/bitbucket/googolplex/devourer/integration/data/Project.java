package org.bitbucket.googolplex.devourer.integration.data;

import com.google.common.base.Preconditions;

import java.util.List;

/**
 * Date: 15.03.13
 * Time: 13:41
 *
 * @author Vladimir Matveev
 */
public class Project {
    public final String name;
    public final List<Module> modules;
    public final List<Library> libraries;

    public Project(String name, List<Module> modules, List<Library> libraries) {
        Preconditions.checkNotNull(name, "Name is null");
        Preconditions.checkNotNull(modules, "Modules are null");
        Preconditions.checkNotNull(libraries, "Libraries are null");

        this.name = name;
        this.modules = modules;
        this.libraries = libraries;
    }
}
