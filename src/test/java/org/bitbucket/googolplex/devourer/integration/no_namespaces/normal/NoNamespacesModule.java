package org.bitbucket.googolplex.devourer.integration.no_namespaces.normal;

import org.bitbucket.googolplex.devourer.configuration.actions.ActionAfter;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAt;
import org.bitbucket.googolplex.devourer.configuration.modular.AbstractMappingModule;
import org.bitbucket.googolplex.devourer.contexts.AttributesContext;
import org.bitbucket.googolplex.devourer.integration.data.Library;
import org.bitbucket.googolplex.devourer.integration.data.Module;
import org.bitbucket.googolplex.devourer.integration.data.Project;
import org.bitbucket.googolplex.devourer.integration.data.SourceFile;
import org.bitbucket.googolplex.devourer.stacks.Stacks;

import java.util.List;

/**
 * Date: 15.03.13
 * Time: 14:12
 *
 * @author Vladimir Matveev
 */
public class NoNamespacesModule extends AbstractMappingModule {
    @Override
    protected void configure() {
        on("/project").doAfter(new ActionAfter() {
            @Override
            public void act(Stacks stacks, AttributesContext context) {
                List<Library> libraries = stacks.get("project-libraries").popList();
                List<Module> modules = stacks.get("modules").popList();
                String name = context.attribute("name").get();
                stacks.push(new Project(name, modules, libraries));
            }
        });

        on("/project/libraries/library").doAfter(new ActionAfter() {
            @Override
            public void act(Stacks stacks, AttributesContext context) {
                stacks.get("project-libraries").push(
                    new Library(context.attribute("groupId").get(),
                                context.attribute("artifactId").get(),
                                context.attribute("version").get())
                );
            }
        });

        on("/project/module").doAfter(new ActionAfter() {
            @Override
            public void act(Stacks stacks, AttributesContext context) {
                stacks.get("modules").push(
                    new Module(context.attribute("name").get(),
                               stacks.get("files").<SourceFile>popList(),
                               stacks.get("libraries").<Library>popList())
                );
            }
        });

        on("/project/module/files/file").doAt(new ActionAt() {
            @Override
            public void act(Stacks stacks, AttributesContext context, String body) {
                stacks.get("files").push(
                    new SourceFile(
                        context.attribute("name").get(),
                        context.attribute("type").get(),
                        body
                    )
                );
            }
        });

        on("/project/module/libraries/library").doAfter(new ActionAfter() {
            @Override
            public void act(Stacks stacks, AttributesContext context) {
                stacks.get("libraries").push(
                    new Library(context.attribute("groupId").get(),
                                context.attribute("artifactId").get(),
                                context.attribute("version").get())
                );
            }
        });
    }
}
