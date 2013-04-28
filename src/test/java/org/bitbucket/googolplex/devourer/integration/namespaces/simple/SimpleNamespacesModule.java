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

package org.bitbucket.googolplex.devourer.integration.namespaces.simple;

import org.bitbucket.googolplex.devourer.configuration.actions.EnhancedAction;
import org.bitbucket.googolplex.devourer.configuration.modular.AbstractMappingModule;
import org.bitbucket.googolplex.devourer.integration.data.Library;
import org.bitbucket.googolplex.devourer.integration.data.Module;
import org.bitbucket.googolplex.devourer.integration.data.Project;
import org.bitbucket.googolplex.devourer.integration.data.SourceFile;

import java.util.List;

/**
 * Date: 28.04.13
 * Time: 19:58
 *
 * @author Vladimir Matveev
 */
public class SimpleNamespacesModule extends AbstractMappingModule {
    @Override
    protected void configure() {
        namespaceContext()
            .map("urn:project").to("pr");

        on("/pr:project").doAfter(new EnhancedAction() {
            @Override
            public void act() {
                List<Library> libraries = get("project-libraries").popList();
                List<Module> modules = get("modules").popList();
                String name = attribute("name").get();
                push(new Project(name, modules, libraries));
            }
        });

        on("/pr:project/pr:libraries/pr:library").doAfter(new EnhancedAction() {
            @Override
            public void act() {
                get("project-libraries").push(
                    new Library(attribute("groupId").get(),
                                attribute("artifactId").get(),
                                attribute("version").get())
                );
            }
        });

        on("/pr:project/pr:module").doAfter(new EnhancedAction() {
            @Override
            public void act() {
                get("modules").push(
                    new Module(attribute("name").get(),
                               get("files").<SourceFile>popList(),
                               get("libraries").<Library>popList())
                );
            }
        });

        on("/pr:project/pr:module/pr:files/pr:file").doAt(new EnhancedAction() {
            @Override
            public void act() {
                get("files").push(
                    new SourceFile(
                        attribute("name").get(),
                        attribute("type").get(),
                        body()
                    )
                );
            }
        });

        on("/pr:project/pr:module/pr:libraries/pr:library").doAfter(new EnhancedAction() {
            @Override
            public void act() {
                get("libraries").push(
                    new Library(attributeWithPrefix("groupId", "pr").get(),
                                attribute("artifactId").get(),
                                attribute("version").get())
                );
            }
        });

    }
}
