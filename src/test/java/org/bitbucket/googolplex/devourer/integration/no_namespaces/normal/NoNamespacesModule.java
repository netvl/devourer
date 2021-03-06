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

package org.bitbucket.googolplex.devourer.integration.no_namespaces.normal;

import org.bitbucket.googolplex.devourer.configuration.actions.EnhancedAction;
import org.bitbucket.googolplex.devourer.configuration.modular.AbstractMappingModule;
import org.bitbucket.googolplex.devourer.integration.data.Library;
import org.bitbucket.googolplex.devourer.integration.data.Module;
import org.bitbucket.googolplex.devourer.integration.data.Project;
import org.bitbucket.googolplex.devourer.integration.data.SourceFile;

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
        on("/project").doAfter(new EnhancedAction() {
            @Override
            public void act() {
                List<Library> libraries = get("project-libraries").popList();
                List<Module> modules = get("modules").popList();
                String name = attribute("name").get();
                push(new Project(name, modules, libraries));
            }
        });

        on("/project/libraries/library").doAfter(new EnhancedAction() {
            @Override
            public void act() {
                get("project-libraries").push(
                    new Library(attribute("groupId").get(),
                                attribute("artifactId").get(),
                                attribute("version").get())
                );
            }
        });

        on("/project/module").doAfter(new EnhancedAction() {
            @Override
            public void act() {
                get("modules").push(
                    new Module(attribute("name").get(),
                               get("files").<SourceFile>popList(),
                               get("libraries").<Library>popList())
                );
            }
        });

        on("/project/module/files/file").doAt(new EnhancedAction() {
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

        on("/project/module/libraries/library").doAfter(new EnhancedAction() {
            @Override
            public void act() {
                get("libraries").push(
                    new Library(attribute("groupId").get(),
                                attribute("artifactId").get(),
                                attribute("version").get())
                );
            }
        });
    }
}
