package org.googolplex.devourer.reactions;

import org.googolplex.devourer.contexts.AttributedContext;
import org.googolplex.devourer.contexts.ElementContext;

/**
 * Date: 18.02.13
 * Time: 14:53
 */
public abstract class ModifyTop<T> extends BaseReaction {
    protected void start(T object, AttributedContext context) {
    }

    protected void body(T object, ElementContext context, String body) {

    }

    protected void end(T object, ElementContext context) {
    }
}
