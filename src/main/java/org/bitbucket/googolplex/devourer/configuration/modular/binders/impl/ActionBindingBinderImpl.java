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

package org.bitbucket.googolplex.devourer.configuration.modular.binders.impl;

import com.google.common.base.Preconditions;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAt;
import org.bitbucket.googolplex.devourer.configuration.modular.binders.ActionBindingBuilder;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAfter;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionBefore;
import org.bitbucket.googolplex.devourer.paths.mappings.MappingBuilder;
import org.bitbucket.googolplex.devourer.paths.patterns.PathPatterns;

public class ActionBindingBinderImpl implements ActionBindingBuilder {
    private final MappingBuilder mappingBuilder;
    private final String route;

    ActionBindingBinderImpl(MappingBuilder mappingBuilder,
                            String route) {
        this.mappingBuilder = mappingBuilder;
        this.route = route;
    }

    @Override
    public ActionBindingBinderImpl doBefore(ActionBefore action) {
        Preconditions.checkNotNull(action, "Action is null");

        this.mappingBuilder.add(PathPatterns.fromString(route), action);
        return this;
    }

    @Override
    public ActionBindingBinderImpl doAt(ActionAt action) {
        Preconditions.checkNotNull(action, "Action is null");

        this.mappingBuilder.add(PathPatterns.fromString(route), action);
        return this;
    }

    @Override
    public ActionBindingBinderImpl doAfter(ActionAfter action) {
        Preconditions.checkNotNull(action, "Action is null");

        this.mappingBuilder.add(PathPatterns.fromString(route), action);
        return this;
    }
}
