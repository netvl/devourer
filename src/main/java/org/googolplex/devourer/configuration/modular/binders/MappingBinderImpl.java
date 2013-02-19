package org.googolplex.devourer.configuration.modular.binders;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import org.googolplex.devourer.paths.Path;
import org.googolplex.devourer.paths.PathMapping;
import org.googolplex.devourer.configuration.reactions.ReactionAfter;
import org.googolplex.devourer.configuration.reactions.ReactionAt;
import org.googolplex.devourer.configuration.reactions.ReactionBefore;

import java.util.HashMap;
import java.util.List;
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
        Map<Path, PathMapping> builder = new HashMap<Path, PathMapping>();
        for (Path path : Iterables.concat(beforeMappings.keySet(), atMappings.keySet(), afterMappings.keySet())) {
            // An order is implicit here
            List<ReactionBefore> befores = ImmutableList.copyOf(beforeMappings.get(path));
            List<ReactionAt> ats = ImmutableList.copyOf(atMappings.get(path));
            List<ReactionAfter> afters = ImmutableList.copyOf(afterMappings.get(path));

            PathMapping mapping = new PathMapping(befores, ats, afters);
            builder.put(path, mapping);
        }
        return ImmutableMap.copyOf(ImmutableMap.copyOf(builder));
    }
}
