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

import org.googolplex.devourer.configuration.modular.binders.MappingBinder;

/**
 * This interface is used by Devourer to configure its mappings. You usually should not implement
 * this interface directly but extend {@link AbstractMappingModule} abstract class instead. It provides
 * small DSL to assist in mapping configuration.
 */
public interface MappingModule {
    /**
     * Called to configure Devourer mappings.
     *
     * @param binder an instance of {@link MappingBinder} interface which will contain mapping configuration
     */
    void configure(MappingBinder binder);
}
