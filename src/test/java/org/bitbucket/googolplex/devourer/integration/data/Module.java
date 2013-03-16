package org.bitbucket.googolplex.devourer.integration.data;

import com.google.common.base.Preconditions;

import java.util.List;

/**
 * Date: 15.03.13
 * Time: 13:42
 *
 * @author Vladimir Matveev
 */
public class Module {
    public final String name;
    public final List<SourceFile> sourceFiles;
    public final List<Library> libraries;

    public Module(String name, List<SourceFile> sourceFiles, List<Library> libraries) {
        Preconditions.checkNotNull(name, "Name is null");
        Preconditions.checkNotNull(sourceFiles, "Source files are null");
        Preconditions.checkNotNull(libraries, "Libraries  are null");

        this.name = name;
        this.sourceFiles = sourceFiles;
        this.libraries = libraries;
    }
}
