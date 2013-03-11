package org.bitbucket.googolplex.devourer.configuration.annotated.internal;

import com.google.common.base.Optional;

/**
 * A container for information about annotated method parameter.
 */
public class ParameterInfo {
    public final ParameterKind kind;
    public final Optional<String> argument;

    public ParameterInfo(ParameterKind kind, String argument) {
        this.kind = kind;
        this.argument = Optional.of(argument);
    }

    public ParameterInfo(ParameterKind kind) {
        this.kind = kind;
        this.argument = Optional.absent();
    }
}
