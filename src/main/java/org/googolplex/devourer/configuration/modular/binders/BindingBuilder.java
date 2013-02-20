package org.googolplex.devourer.configuration.modular.binders;

import com.google.common.base.Preconditions;
import org.googolplex.devourer.paths.Path;
import org.googolplex.devourer.configuration.reactions.ReactionAfter;
import org.googolplex.devourer.configuration.reactions.ReactionAt;
import org.googolplex.devourer.configuration.reactions.ReactionBefore;

public class BindingBuilder implements ReactionBindingBuilder {
    private final MappingBinderImpl mappingBinder;
    private final String route;

    protected BindingBuilder(MappingBinderImpl mappingBinder, String route) {
        this.mappingBinder = mappingBinder;
        this.route = route;
    }

    @Override
    public BindingBuilder doBefore(ReactionBefore reaction) {
        Preconditions.checkNotNull(reaction, "Reaction is null");
        mappingBinder.beforeMappings.put(Path.fromString(route), reaction);
        return this;
    }

    @Override
    public BindingBuilder doAt(ReactionAt reaction) {
        Preconditions.checkNotNull(reaction, "Reaction is null");
        mappingBinder.atMappings.put(Path.fromString(route), reaction);
        return this;
    }

    @Override
    public BindingBuilder doAfter(ReactionAfter reaction) {
        Preconditions.checkNotNull(reaction, "Reaction is null");
        mappingBinder.afterMappings.put(Path.fromString(route), reaction);
        return this;
    }
}
