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

import com.google.common.base.Optional;
import org.bitbucket.googolplex.devourer.contexts.namespaces.NamespaceContext;
import org.bitbucket.googolplex.devourer.paths.ExactPath;

/**
 * Date: 19.02.13
 * Time: 10:53
 */
public interface PathMapping {
    Optional<ActionBundle> lookup(ExactPath path, NamespaceContext namespaceContext);
}
