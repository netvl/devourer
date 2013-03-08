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
 * Date: 07.03.13
 * Time: 22:04
 *
 * @author Vladimir Matveev
 */
public class ExactPath {
    public final List<QualifiedName> parts;

    private ExactPath(List<QualifiedName> parts) {
        this.parts = parts;
    }

    public ExactPath resolve(QualifiedName name) {
        Preconditions.checkNotNull(name, "Name is null");

        ImmutableList.Builder<QualifiedName> result = ImmutableList.builder();
        return new ExactPath(result.addAll(this.parts).add(name).build());
    }

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

    public static ExactPath root() {
        return new ExactPath(ImmutableList.<QualifiedName>of());
    }

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
