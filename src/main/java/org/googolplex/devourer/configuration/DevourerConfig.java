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

package org.googolplex.devourer.configuration;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 19.02.13
 * Time: 15:12
 */
public class DevourerConfig {
    public final boolean stripSpaces;
    public final Map<String, String> staxConfig;

    public DevourerConfig(boolean stripSpaces, Map<String, String> staxConfig) {
        Preconditions.checkNotNull(staxConfig, "StAX config map is null");

        this.stripSpaces = stripSpaces;
        this.staxConfig = staxConfig;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean stripSpaces = false;
        private Map<String, String> staxConfig = new HashMap<String, String>();

        private Builder() {
        }

        public DevourerConfig build() {
            return new DevourerConfig(stripSpaces, staxConfig);
        }

        public Builder setStripSpaces(boolean stripSpaces) {
            this.stripSpaces = stripSpaces;
            return this;
        }

        public Builder setStaxProperty(String name, String value) {
            Preconditions.checkNotNull(name, "StAX property name is null");
            Preconditions.checkNotNull(value, "StAX value name is null");

            this.staxConfig.put(name, value);
            return this;
        }
    }
}
