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

package org.bitbucket.googolplex.devourer.configuration.modular.binders;

import com.google.common.base.Preconditions;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAt;
import org.bitbucket.googolplex.devourer.paths.Path;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAfter;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionBefore;

public class BindingBuilder implements ActionBindingBuilder {
    private final MappingBinderImpl mappingBinder;
    private final String route;

    protected BindingBuilder(MappingBinderImpl mappingBinder, String route) {
        this.mappingBinder = mappingBinder;
        this.route = route;
    }

    @Override
    public BindingBuilder doBefore(ActionBefore action) {
        Preconditions.checkNotNull(action, "Action is null");
        mappingBinder.beforeMappings.put(Path.fromString(route), action);
        return this;
    }

    @Override
    public BindingBuilder doAt(ActionAt action) {
        Preconditions.checkNotNull(action, "Action is null");
        mappingBinder.atMappings.put(Path.fromString(route), action);
        return this;
    }

    @Override
    public BindingBuilder doAfter(ActionAfter action) {
        Preconditions.checkNotNull(action, "Action is null");
        mappingBinder.afterMappings.put(Path.fromString(route), action);
        return this;
    }
}
