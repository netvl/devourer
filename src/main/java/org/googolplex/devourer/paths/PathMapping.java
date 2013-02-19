package org.googolplex.devourer.paths;

import com.google.common.base.Preconditions;
import org.googolplex.devourer.configuration.reactions.ReactionAfter;
import org.googolplex.devourer.configuration.reactions.ReactionAt;
import org.googolplex.devourer.configuration.reactions.ReactionBefore;

import java.util.List;

/**
 * Date: 19.02.13
 * Time: 10:53
 */
public class PathMapping {
    public final List<ReactionBefore> befores;
    public final List<ReactionAt> ats;
    public final List<ReactionAfter> afters;

    public PathMapping(List<ReactionBefore> befores, List<ReactionAt> ats, List<ReactionAfter> afters) {
        Preconditions.checkNotNull(befores, "ReactionBefore list is null");
        Preconditions.checkNotNull(ats, "ReactionAt list is null");
        Preconditions.checkNotNull(afters, "ReactionAfter list is null");

        this.befores = befores;
        this.ats = ats;
        this.afters = afters;
    }
}
