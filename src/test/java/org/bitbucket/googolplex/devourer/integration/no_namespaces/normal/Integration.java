package org.bitbucket.googolplex.devourer.integration.no_namespaces.normal;

import com.google.common.collect.ImmutableList;
import org.bitbucket.googolplex.devourer.Devourer;
import org.bitbucket.googolplex.devourer.Devourers;
import org.bitbucket.googolplex.devourer.integration.Checks;
import org.bitbucket.googolplex.devourer.integration.data.Library;
import org.bitbucket.googolplex.devourer.integration.data.Module;
import org.bitbucket.googolplex.devourer.integration.data.Project;
import org.bitbucket.googolplex.devourer.integration.data.SourceFile;
import org.bitbucket.googolplex.devourer.stacks.Stacks;
import org.junit.Test;

/**
 * Date: 15.03.13
 * Time: 14:15
 *
 * @author Vladimir Matveev
 */
public class Integration {
    public static final String DOCUMENT =
        "<project name=\"project-name\">\n" +
        "    <libraries>\n" +
        "        <library groupId=\"org.example\" artifactId=\"name\" version=\"0.1\"/>\n" +
        "        <library groupId=\"com.example\" artifactId=\"cool-lib\" version=\"999\"/>\n" +
        "    </libraries>\n" +
        "    <module name=\"module-1\">\n" +
        "        <files>\n" +
        "            <file name=\"somefile.java\" type=\"java\">\n" +
        "                Some java class\n" +
        "            </file>\n" +
        "            <file name=\"another_file.java\" type=\"java\">\n" +
        "                Another java class\n" +
        "            </file>\n" +
        "            <file name=\"config.xml\" type=\"xml\">\n" +
        "                Weird XML config\n" +
        "            </file>\n" +
        "        </files>\n" +
        "        <libraries>\n" +
        "            <library groupId=\"junit\" artifactId=\"junit\" version=\"1.9.5\"/>\n" +
        "        </libraries>\n" +
        "    </module>\n" +
        "    <module name=\"module-2\">\n" +
        "        <files>\n" +
        "            <file name=\"program.js\" type=\"javascript\">\n" +
        "                JavaScript program\n" +
        "            </file>\n" +
        "            <file name=\"style.css\" type=\"css\">\n" +
        "                Cascading style sheet\n" +
        "            </file>\n" +
        "        </files>\n" +
        "    </module>\n" +
        "</project>";

    public static final Project expectedProject = new Project(
        "project-name",
        ImmutableList.of(
            new Module(
                "module-1",
                ImmutableList.of(
                    new SourceFile("somefile.java", "java", "Some java class"),
                    new SourceFile("another_file.java", "java", "Another java class"),
                    new SourceFile("config.xml", "xml", "Weird XML config")
                ),
                ImmutableList.of(
                    new Library("junit", "junit", "1.9.5")
                )
            ),
            new Module(
                "module-2",
                ImmutableList.of(
                    new SourceFile("program.js", "javascript", "JavaScript program"),
                    new SourceFile("style.css", "css", "Cascading style sheet")
                ),
                ImmutableList.<Library>of()
            )
        ),
        ImmutableList.of(
            new Library("org.example", "name", "0.1"),
            new Library("com.example", "cool-lib", "999")
        )
    );

    @Test
    public void testModularConfig() throws Exception {
        Devourer devourer = Devourers.create(new NoNamespacesModule());

        Stacks stacks = devourer.parse(DOCUMENT);
        Project project = stacks.pop();

        Checks.checkProject(project, expectedProject);
    }

    @Test
    public void testAnnotatedConfig() throws Exception {
        Devourer devourer = Devourers.create(new NoNamespacesAnnotatedConfig());

        Stacks stacks = devourer.parse(DOCUMENT);
        Project project = stacks.pop();

        Checks.checkProject(project, expectedProject);
    }

}
