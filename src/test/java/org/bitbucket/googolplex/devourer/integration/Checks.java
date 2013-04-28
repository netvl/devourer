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

package org.bitbucket.googolplex.devourer.integration;

import org.bitbucket.googolplex.devourer.integration.data.Library;
import org.bitbucket.googolplex.devourer.integration.data.Module;
import org.bitbucket.googolplex.devourer.integration.data.Project;
import org.bitbucket.googolplex.devourer.integration.data.SourceFile;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Date: 28.04.13
 * Time: 19:57
 *
 * @author Vladimir Matveev
 */
public class Checks {
    public static void checkProject(Project project, Project expectedProject) {
        assertEquals(expectedProject.name, project.name);

        checkLibraries(project.libraries, expectedProject.libraries);
        checkModules(project.modules, expectedProject.modules);
    }

    public static void checkModules(List<Module> modules, List<Module> expectedModules) {
        assertEquals(expectedModules.size(), modules.size());

        for (int i = 0; i < expectedModules.size(); ++i) {
            Module module = modules.get(i);
            Module expectedModule = expectedModules.get(i);

            assertEquals(expectedModule.name, module.name);

            checkSourceFiles(expectedModule.sourceFiles, module.sourceFiles);

            checkLibraries(expectedModule.libraries, module.libraries);
        }
    }

    public static void checkSourceFiles(List<SourceFile> files, List<SourceFile> expectedFiles) {
        assertEquals(expectedFiles.size(), files.size());

        for (int i = 0; i < expectedFiles.size(); i++) {
            SourceFile file = files.get(i);
            SourceFile expectedFile = expectedFiles.get(i);

            assertEquals(expectedFile.name, file.name);
            assertEquals(expectedFile.type, file.type);
            assertEquals(expectedFile.content, file.content);
        }
    }

    public static void checkLibraries(List<Library> libraries, List<Library> expectedLibraries) {
        assertEquals(expectedLibraries.size(), libraries.size());

        for (int i = 0; i < expectedLibraries.size(); ++i) {
            Library library = libraries.get(i);
            Library expectedLibrary = expectedLibraries.get(i);

            assertEquals(expectedLibrary.groupId, library.groupId);
            assertEquals(expectedLibrary.artifactId, library.artifactId);
            assertEquals(expectedLibrary.version, library.version);
        }
    }
}
