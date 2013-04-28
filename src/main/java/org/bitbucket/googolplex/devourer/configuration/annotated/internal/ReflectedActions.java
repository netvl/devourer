package org.bitbucket.googolplex.devourer.configuration.annotated.internal;

import com.google.common.base.Optional;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAfter;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAt;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionBefore;
import org.bitbucket.googolplex.devourer.contexts.ElementContext;
import org.bitbucket.googolplex.devourer.stacks.Stacks;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Action interfaces implementations delegating their {@code act} method to annotated configuration object.
 */
public final class ReflectedActions {
    public static class At extends AbstractReflectedAction implements ActionAt {
        public At(Object object, Method method, Optional<String> stack, List<ParameterInfo> parameterInfos) {
            super(object, method, stack, parameterInfos);
        }

        @Override
        public void act(Stacks stacks, ElementContext context, String body) {
            invokeMethod(stacks, context, Optional.of(body));
        }
    }

    public static class After extends AbstractReflectedAction implements ActionAfter {
        public After(Object object, Method method, Optional<String> stack, List<ParameterInfo> parameterInfos) {
            super(object, method, stack, parameterInfos);
        }

        @Override
        public void act(Stacks stacks, ElementContext context) {
            invokeMethod(stacks, context, Optional.<String>absent());
        }
    }

    public static class Before extends AbstractReflectedAction implements ActionBefore {
        public Before(Object object, Method method, Optional<String> stack, List<ParameterInfo> parameterInfos) {
            super(object, method, stack, parameterInfos);
        }

        @Override
        public void act(Stacks stacks, ElementContext context) {
            invokeMethod(stacks, context, Optional.<String>absent());
        }
    }
}
