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
 * A container class for Devourer configuration. Currently the following parameters can be configured:
 * <ul>
 *     <li>an option whether the Devourer should trim side spaces of body content before providing it
 *     to the reactions - {@code true} by default;</li>
 *     <li>StAX parser parameters, as defined in {@link javax.xml.stream.XMLInputFactory} documentation.</li>
 * </ul>
 *
 * <p>A {@link Builder} can be used to construct instances of this class.</p>
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
        private boolean stripSpaces = true;
        private Map<String, String> staxConfig = new HashMap<String, String>();

        private Builder() {
        }

        /**
         * @return new configuration instance created from the accumulated values
         */
        public DevourerConfig build() {
            return new DevourerConfig(stripSpaces, staxConfig);
        }

        /**
         * Sets whether Devourer should strip side spaces around element content before providing it to the
         * reaction. True by default; it is usually what is expected from the XML parser.
         *
         * @return this object
         */
        public Builder setStripSpaces(boolean stripSpaces) {
            this.stripSpaces = stripSpaces;
            return this;
        }

        /**
         * Sets StAX property given its name and value. See {@link javax.xml.stream.XMLInputFactory} documentation
         * for the list of available properties.
         *
         * @param name property name
         * @param value property value
         * @return this object
         */
        public Builder setStaxProperty(String name, String value) {
            Preconditions.checkNotNull(name, "StAX property name is null");
            Preconditions.checkNotNull(value, "StAX property value is null");

            this.staxConfig.put(name, value);
            return this;
        }
    }
}
