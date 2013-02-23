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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.googolplex.devourer.configuration.actions.ActionAt;
import org.googolplex.devourer.paths.Mappings;
import org.googolplex.devourer.paths.Path;
import org.googolplex.devourer.paths.PathMapping;
import org.googolplex.devourer.configuration.actions.ActionAfter;
import org.googolplex.devourer.configuration.actions.ActionBefore;

import java.util.Map;

public class MappingBinderImpl implements MappingBinder {
    protected final ListMultimap<Path, ActionBefore> beforeMappings = ArrayListMultimap.create();
    protected final ListMultimap<Path, ActionAfter> afterMappings = ArrayListMultimap.create();
    protected final ListMultimap<Path, ActionAt> atMappings = ArrayListMultimap.create();

    @Override
    public ActionBindingBuilder on(String route) {
        return new BindingBuilder(this, route);
    }

    public Map<Path, PathMapping> mappings() {
        return Mappings.combineMappings(beforeMappings, atMappings, afterMappings);
    }

}
