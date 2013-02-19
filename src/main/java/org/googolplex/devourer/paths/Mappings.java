package org.googolplex.devourer.paths;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import org.googolplex.devourer.configuration.reactions.ReactionAfter;
import org.googolplex.devourer.configuration.reactions.ReactionAt;
import org.googolplex.devourer.configuration.reactions.ReactionBefore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 19.02.13
 * Time: 19:14
 *
 * @author Vladimir Matveev
 */
public final class Mappings {
    private Mappings() {
    }

    public static Map<Path, PathMapping> combineMappings(ListMultimap<Path, ReactionBefore> beforeMappings,
                                                         ListMultimap<Path, ReactionAt> atMappings,
                                                         ListMultimap<Path, ReactionAfter> afterMappings) {
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
