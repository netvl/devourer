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

package org.bitbucket.googolplex.devourer.paths.patterns.parts;

import com.google.common.base.Preconditions;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.contexts.namespaces.QualifiedName;
import org.bitbucket.googolplex.devourer.paths.patterns.PathPattern;
import org.bitbucket.googolplex.devourer.paths.PathsConstants;

import java.util.List;

/**
 * Date: 09.03.13
 * Time: 0:06
 *
 * @author Vladimir Matveev
 */
public abstract class PatternElement implements PathPattern {
    // TODO: mayve split into several classes

    public boolean isLiteral() {
        return false;
    }

    public boolean isGlobalWildcard() {
        return false;
    }

    public abstract String toString();

    public static PatternElement fromString(String string) {
        Preconditions.checkNotNull(string, "String is empty");

        if (PathsConstants.MULTIWILDCARD.equals(string)) {
            return multiWildcard();
        } else {
            return literal(LiteralName.fromString(string));
        }
    }

    public static PatternElement literal(LiteralName name) {
        Preconditions.checkNotNull(name, "Name is null");

        return new LiteralElement(name);
    }

    public static PatternElement literal(String name) {
        return literal(LiteralName.localOnly(name));
    }

    public static PatternElement literal(String name, String prefix) {
        return literal(LiteralName.withPrefix(name, prefix));
    }

    public static PatternElement multiWildcard() {
        return new MultiWildcardElement();
    }

    private static class LiteralElement extends PatternElement {
        private final LiteralName name;

        private LiteralElement(LiteralName name) {
            this.name = name;
        }

        @Override
        public boolean isLiteral() {
            return true;
        }

        @Override
        public String toString() {
            return name.toString();
        }

        @Override
        public boolean matches(List<QualifiedName> names, NamespaceContext context) {
            return name.matches(names, context);
        }
    }

    private static class MultiWildcardElement extends PatternElement {
        private MultiWildcardElement() {
        }

        @Override
        public boolean isGlobalWildcard() {
            return true;
        }

        @Override
        public String toString() {
            return PathsConstants.MULTIWILDCARD;
        }

        @Override
        public boolean matches(List<QualifiedName> names, NamespaceContext context) {
            return true;  // Global wildcard matches everything
        }
    }
}
