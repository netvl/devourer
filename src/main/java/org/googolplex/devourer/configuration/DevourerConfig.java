package org.googolplex.devourer.configuration;

/**
 * Date: 19.02.13
 * Time: 15:12
 */
public class DevourerConfig {
    public final boolean stripSpaces;

    public DevourerConfig(boolean stripSpaces) {
        this.stripSpaces = stripSpaces;
    }

    public static class Builder {
        private boolean stripSpaces = false;

        public DevourerConfig build() {
            return new DevourerConfig(stripSpaces);
        }

        public Builder setStripSpaces(boolean stripSpaces) {
            this.stripSpaces = stripSpaces;
            return this;
        }
    }
}
