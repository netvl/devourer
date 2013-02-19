package org.googolplex.devourer.configuration.modular.binders;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.googolplex.devourer.paths.Mappings;
import org.googolplex.devourer.paths.Path;
import org.googolplex.devourer.paths.PathMapping;
import org.googolplex.devourer.configuration.reactions.ReactionAfter;
import org.googolplex.devourer.configuration.reactions.ReactionAt;
import org.googolplex.devourer.configuration.reactions.ReactionBefore;

import java.util.Map;

/**
 * Date: 19.02.13
 * Time: 14:03
 */
public class MappingBinderImpl implements MappingBinder {
    protected final ListMultimap<Path, ReactionBefore> beforeMappings = ArrayListMultimap.create();
    protected final ListMultimap<Path, ReactionAfter> afterMappings = ArrayListMultimap.create();
    protected final ListMultimap<Path, ReactionAt> atMappings = ArrayListMultimap.create();

    @Override
    public ReactionBindingBuilder on(String route) {
        return new BindingBuilder(this, route);
    }

    public Map<Path, PathMapping> mappings() {
        return Mappings.combineMappings(beforeMappings, atMappings, afterMappings);
    }

}
