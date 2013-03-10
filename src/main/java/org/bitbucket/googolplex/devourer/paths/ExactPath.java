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

package org.bitbucket.googolplex.devourer.paths;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedName;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedNames;

import java.util.List;

/**
 * Represents a path inside XML document. Essentially this is a list of {@link QualifiedName}s of XML nodes.
 */
public class ExactPath {
    public final List<QualifiedName> parts;

    ExactPath(List<QualifiedName> parts) {
        this.parts = parts;
    }

    /**
     * Returns a copy of this instance with the given name appended to the end of the names list.
     *
     * @param name qualified name
     * @return new exact path which is one element longer than this object
     */
    public ExactPath resolve(QualifiedName name) {
        Preconditions.checkNotNull(name, "Name is null");

        ImmutableList.Builder<QualifiedName> result = ImmutableList.builder();
        return new ExactPath(result.addAll(this.parts).add(name).build());
    }

    /**
     * Returns a copy of this instance with the last element chopped off. When there are no elements in the path,
     * returns this instance.
     *
     * @return new exact path which is one element shorter than this object, if it is possible
     */
    public ExactPath moveUp() {
        if (parts.isEmpty()) {
            return this;
        } else {
            return new ExactPath(parts.subList(0, parts.size()-1));
        }
    }

    @Override
    public String toString() {
        return "/" + PathsConstants.PATH_JOINER.join(parts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExactPath exactPath = (ExactPath) o;

        return parts.equals(exactPath.parts);
    }

    @Override
    public int hashCode() {
        return parts.hashCode();
    }

    /**
     * @return a path representing root node, i.e. {@code "/"}.
     */
    public static ExactPath root() {
        return new ExactPath(ImmutableList.<QualifiedName>of());
    }

    /**
     * Parses given string and returns exact path which is represented by it. TODO: description of the grammar
     *
     * @param string a string representing path inside XML tree
     * @return new exact path
     */
    public static ExactPath fromString(String string) {
        Preconditions.checkNotNull(string, "String is null");

        ImmutableList.Builder<QualifiedName> result = ImmutableList.builder();

        Iterable<String> parts = PathsConstants.PATH_SPLITTER.split(string);
        for (String part : parts) {
            QualifiedName name = QualifiedNames.fromString(part);
            result.add(name);
        }

        return new ExactPath(result.build());
    }
}
