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
         * reaction.
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
            Preconditions.checkNotNull(value, "StAX value name is null");

            this.staxConfig.put(name, value);
            return this;
        }
    }
}
