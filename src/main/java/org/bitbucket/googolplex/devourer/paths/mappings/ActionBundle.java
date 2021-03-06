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

package org.bitbucket.googolplex.devourer.paths.mappings;

import com.google.common.base.Preconditions;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAfter;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAt;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionBefore;

import java.util.List;

/**
 * Date: 19.02.13
 * Time: 10:53
 */
public class ActionBundle {
    public final List<ActionBefore> befores;
    public final List<ActionAt> ats;
    public final List<ActionAfter> afters;

    public ActionBundle(List<ActionBefore> befores, List<ActionAt> ats, List<ActionAfter> afters) {
        Preconditions.checkNotNull(befores, "ActionBefore list is null");
        Preconditions.checkNotNull(ats, "ActionAt list is null");
        Preconditions.checkNotNull(afters, "ActionAfter list is null");

        this.befores = befores;
        this.ats = ats;
        this.afters = afters;
    }
}
