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

package org.googolplex.devourer.configuration.modular;

import com.google.common.base.Preconditions;
import org.googolplex.devourer.configuration.modular.binders.ReactionBindingBuilder;
import org.googolplex.devourer.configuration.modular.binders.MappingBinder;

/**
 * Date: 19.02.13
 * Time: 13:55
 */
public abstract class AbstractMappingModule implements MappingModule {
    private MappingBinder binder = null;

    protected final ReactionBindingBuilder on(String route) {
        return binder.on(route);
    }

    @Override
    public synchronized final void configure(MappingBinder binder) {
        Preconditions.checkState(this.binder == null, "Cannot re-enter configuration process");
        Preconditions.checkNotNull(binder, "Binder is null");

        this.binder = binder;
        try {
            configure();
        } finally {
            this.binder = null;
        }
    }

    protected abstract void configure();
}
