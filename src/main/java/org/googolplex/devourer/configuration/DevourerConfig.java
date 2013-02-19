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
