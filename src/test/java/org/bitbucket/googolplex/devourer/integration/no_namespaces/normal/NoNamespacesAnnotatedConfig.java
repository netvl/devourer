package org.bitbucket.googolplex.devourer.integration.no_namespaces.normal;

import org.bitbucket.googolplex.devourer.configuration.annotated.annotations.After;
import org.bitbucket.googolplex.devourer.configuration.annotated.annotations.At;
import org.bitbucket.googolplex.devourer.configuration.annotated.annotations.PopListFrom;
import org.bitbucket.googolplex.devourer.configuration.annotated.annotations.PushTo;
import org.bitbucket.googolplex.devourer.contexts.AttributesContext;
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
                                 AttributesContext context) {
        return new Project(context.attribute("name").get(), modules, libraries);
    }

    @After("/project/libraries/library")
    @PushTo("project-libraries")
    public Library createLibrary(AttributesContext context) {
        return new Library(context.attribute("groupId").get(),
                           context.attribute("artifactId").get(),
                           context.attribute("version").get());
    }

    @After("/project/module")
    @PushTo("modules")
    public Module createModule(@PopListFrom("files") List<SourceFile> files,
                               @PopListFrom("libraries") List<Library> libraries,
                               AttributesContext context) {
        return new Module(context.attribute("name").get(), files, libraries);
    }

    @At("/project/module/files/file")
    @PushTo("files")
    public SourceFile createFile(AttributesContext context, String body) {
        return new SourceFile(context.attribute("name").get(), context.attribute("type").get(), body);
    }

    @After("/project/module/libraries/library")
    @PushTo("libraries")
    public Library createModuleLibrary(AttributesContext context) {
        return new Library(context.attribute("groupId").get(),
                           context.attribute("artifactId").get(),
                           context.attribute("version").get());
    }
}
