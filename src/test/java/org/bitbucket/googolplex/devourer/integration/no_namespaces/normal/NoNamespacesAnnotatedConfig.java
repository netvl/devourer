package org.bitbucket.googolplex.devourer.integration.no_namespaces.normal;

import org.bitbucket.googolplex.devourer.configuration.annotated.annotations.*;
import org.bitbucket.googolplex.devourer.integration.data.Library;
import org.bitbucket.googolplex.devourer.integration.data.Module;
import org.bitbucket.googolplex.devourer.integration.data.Project;
import org.bitbucket.googolplex.devourer.integration.data.SourceFile;

import java.util.List;

/**
 * Date: 17.03.13
 * Time: 1:18
 *
 * @author Vladimir Matveev
 */
public class NoNamespacesAnnotatedConfig {
    @After("/project")
    public Project createProject(@PopListFrom("project-libraries") List<Library> libraries,
                                 @PopListFrom("modules") List<Module> modules,
                                 @Attribute("name") String name) {
        return new Project(name, modules, libraries);
    }

    @After("/project/libraries/library")
    @PushTo("project-libraries")
    public Library createLibrary(@Attribute("groupId") String groupId,
                                 @Attribute("artifactId") String artifactId,
                                 @Attribute("version") String version) {
        return new Library(groupId, artifactId, version);
    }

    @After("/project/module")
    @PushTo("modules")
    public Module createModule(@PopListFrom("files") List<SourceFile> files,
                               @PopListFrom("libraries") List<Library> libraries,
                               @Attribute("name") String name) {
        return new Module(name, files, libraries);
    }

    @At("/project/module/files/file")
    @PushTo("files")
    public SourceFile createFile(@Attribute("name") String name,
                                 @Attribute("type") String type,
                                 String body) {
        return new SourceFile(name, type, body);
    }

    @After("/project/module/libraries/library")
    @PushTo("libraries")
    public Library createModuleLibrary(@Attribute("groupId") String groupId,
                                       @Attribute("artifactId") String artifactId,
                                       @Attribute("version") String version) {
        return new Library(groupId, artifactId, version);
    }
}
