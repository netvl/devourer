package org.googolplex.devourer.reactions;

/**
 * Date: 18.02.13
 * Time: 22:11
 *
 * @author Vladimir Matveev
 */
public class ReactionMapping {
    public final String route;
    public final Reaction reaction;

    public ReactionMapping(String route, Reaction reaction) {
        this.route = route;
        this.reaction = reaction;
    }

    public static Builder map(String route) {
        return new Builder(route);
    }

    public static class Builder {
        private String route;

        public Builder(String route) {
            this.route = route;
        }

        public ReactionMapping to(Reaction reaction) {
            return new ReactionMapping(route, reaction);
        }
    }
}
