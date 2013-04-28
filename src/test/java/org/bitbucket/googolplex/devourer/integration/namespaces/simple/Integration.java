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

/**
 * Date: 27.04.13
 * Time: 21:45
 *
 * @author Vladimir Matveev
 */
public class Integration {
    public static final String DOCUMENT =
        "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
        "<p:project xmlns:p=\"urn:project\" name=\"project-name\">\n" +
        "    <p:libraries>\n" +
        "        <p:library groupId=\"org.example\" artifactId=\"name\" version=\"0.1\"/>\n" +
        "        <p:library groupId=\"com.example\" artifactId=\"cool-lib\" version=\"999\"/>\n" +
        "    </p:libraries>\n" +
        "    <p:module name=\"module-1\">\n" +
        "        <p:files>\n" +
        "            <p:file name=\"somefile.java\" type=\"java\">\n" +
        "                Some java class\n" +
        "            </p:file>\n" +
        "            <p:file name=\"another_file.java\" type=\"java\">\n" +
        "                Another java class\n" +
        "            </p:file>\n" +
        "            <p:file name=\"config.xml\" type=\"xml\">\n" +
        "                Weird XML config\n" +
        "            </p:file>\n" +
        "        </p:files>\n" +
        "        <p:libraries>\n" +
        "            <p:library groupId=\"junit\" artifactId=\"junit\" version=\"1.9.5\"/>\n" +
        "        </p:libraries>\n" +
        "    </p:module>\n" +
        "    <p:module name=\"module-2\">\n" +
        "        <p:files>\n" +
        "            <p:file name=\"program.js\" type=\"javascript\">\n" +
        "                JavaScript program\n" +
        "            </p:file>\n" +
        "            <p:file name=\"style.css\" type=\"css\">\n" +
        "                Cascading style sheet\n" +
        "            </p:file>\n" +
        "        </p:files>\n" +
        "    </p:module>\n" +
        "</p:project>\n";


}
