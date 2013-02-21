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

package org.googolplex.devourer.configuration.modular.binders;

import com.google.common.base.Preconditions;
import org.googolplex.devourer.paths.Path;
import org.googolplex.devourer.configuration.reactions.ReactionAfter;
import org.googolplex.devourer.configuration.reactions.ReactionAt;
import org.googolplex.devourer.configuration.reactions.ReactionBefore;

public class BindingBuilder implements ReactionBindingBuilder {
    private final MappingBinderImpl mappingBinder;
    private final String route;

    protected BindingBuilder(MappingBinderImpl mappingBinder, String route) {
        this.mappingBinder = mappingBinder;
        this.route = route;
    }

    @Override
    public BindingBuilder doBefore(ReactionBefore reaction) {
        Preconditions.checkNotNull(reaction, "Reaction is null");
        mappingBinder.beforeMappings.put(Path.fromString(route), reaction);
        return this;
    }

    @Override
    public BindingBuilder doAt(ReactionAt reaction) {
        Preconditions.checkNotNull(reaction, "Reaction is null");
        mappingBinder.atMappings.put(Path.fromString(route), reaction);
        return this;
    }

    @Override
    public BindingBuilder doAfter(ReactionAfter reaction) {
        Preconditions.checkNotNull(reaction, "Reaction is null");
        mappingBinder.afterMappings.put(Path.fromString(route), reaction);
        return this;
    }
}
