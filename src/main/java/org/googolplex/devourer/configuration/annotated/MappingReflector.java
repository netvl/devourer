package org.googolplex.devourer.configuration.annotated;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.googolplex.devourer.Stacks;
import org.googolplex.devourer.configuration.annotated.annotations.After;
import org.googolplex.devourer.configuration.annotated.annotations.At;
import org.googolplex.devourer.configuration.annotated.annotations.Before;
import org.googolplex.devourer.configuration.annotated.annotations.Peek;
import org.googolplex.devourer.configuration.annotated.annotations.PeekFrom;
import org.googolplex.devourer.configuration.annotated.annotations.Pop;
import org.googolplex.devourer.configuration.annotated.annotations.PopFrom;
import org.googolplex.devourer.configuration.annotated.annotations.PushTo;
import org.googolplex.devourer.configuration.reactions.ReactionAfter;
import org.googolplex.devourer.configuration.reactions.ReactionAt;
import org.googolplex.devourer.configuration.reactions.ReactionBefore;
import org.googolplex.devourer.contexts.AttributesContext;
import org.googolplex.devourer.contexts.ElementContext;
import org.googolplex.devourer.exceptions.DevourerException;
import org.googolplex.devourer.exceptions.MappingException;
import org.googolplex.devourer.paths.Mappings;
import org.googolplex.devourer.paths.Path;
import org.googolplex.devourer.paths.PathMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link MappingReflector} class supports creation of mappings from a class annotated in a special way.
 * The syntax and overall view of such class is heavily inspired by Spring controllers configuration.
 *
 * <p>A class of supplied object should contain methods annotated either with {@link Before}, {@link At} or
 * {@link After} annotation, each of which designating either before-, at- or after-action. These annotations
 * accept mandatory string parameter, a path to the element inside XML document this method will handle.</p>
 *
 * <p>Each method optionally can return a value. Such method can optionally be annotated with {@link PushTo}
 * annotations, which accepts string argument, a stack name. If non-void method is not annotated with {@link PushTo},
 * it is considered as being annotated with {@code @PushTo(Stacks.DEFAULT_STACK}. The return value of
 * such method will be pushed onto the corresponding stack during parsing process.</p>
 *
 * <p>Each method can take variable number of arguments.</p>
 */
public class MappingReflector {
    public Map<Path, PathMapping> collectMappings(Object object) {
        Preconditions.checkNotNull(object, "Object is null");

        // Extract object class
        Class<?> clazz = object.getClass();

        // Prepare temporary collections
        ListMultimap<Path, ReactionBefore> beforeMappings = ArrayListMultimap.create();
        ListMultimap<Path, ReactionAfter> afterMappings = ArrayListMultimap.create();
        ListMultimap<Path, ReactionAt> atMappings = ArrayListMultimap.create();

        // Loop through all available methods
        for (Method method : clazz.getMethods()) {
            // Only inspect a method annotated with @Before, @At or @After
            if (method.isAnnotationPresent(Before.class) ||
                method.isAnnotationPresent(At.class) ||
                method.isAnnotationPresent(After.class)) {

                // Check whether the method returns something and try to get stack name where to push
                Optional<String> stack = Optional.absent();
                if (method.getReturnType() != void.class) {
                    String stackName = Stacks.DEFAULT_STACK;
                    if (method.isAnnotationPresent(PushTo.class)) {
                        stackName = method.getAnnotation(PushTo.class).value();
                    }
                    stack = Optional.of(stackName);
                }

                // Get path to the node
                String route;
                if (method.isAnnotationPresent(Before.class)) {
                    route = method.getAnnotation(Before.class).value();
                } else if (method.isAnnotationPresent(At.class)) {
                    route = method.getAnnotation(At.class).value();
                } else {
                    route = method.getAnnotation(After.class).value();
                }
                Path path = Path.fromString(route);

                // Inspect parameters and construct a list of parameter information pieces
                List<ParameterInfo> parameterInfos = new ArrayList<ParameterInfo>();

                // Parameters information
                Class<?>[] parameterTypes = method.getParameterTypes();
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();

                // Loop through all the parameters
                for (int i = 0; i < parameterTypes.length; ++i) {
                    Class<?> type = parameterTypes[i];
                    Annotation[] annotations = parameterAnnotations[i];

                    // Parameter is of context type
                    if (type == AttributesContext.class || type == ElementContext.class) {
                        parameterInfos.add(new ParameterInfo(ParameterKind.CONTEXT));

                    // Parameter is of string type -- body
                    } else if (type == String.class) {
                        if (!method.isAnnotationPresent(At.class)) {
                            throw new MappingException("Requested body not in @At method");
                        }
                        parameterInfos.add(new ParameterInfo(ParameterKind.BODY));

                    // Otherwise it can only be a stack checking operation
                    } else {
                        // At least one annotation should be present
                        if (annotations.length > 0) {
                            // We will analyze only the first one
                            Annotation annotation = annotations[0];
                            if (annotation.annotationType() == Pop.class) {
                                parameterInfos.add(new ParameterInfo(ParameterKind.POP,
                                                                     Stacks.DEFAULT_STACK));
                            } else if (annotation.annotationType() == PopFrom.class) {
                                parameterInfos.add(new ParameterInfo(ParameterKind.POP,
                                                                     ((PopFrom) annotation).value()));
                            } else if (annotation.annotationType() == Peek.class) {
                                parameterInfos.add(new ParameterInfo(ParameterKind.PEEK,
                                                                     Stacks.DEFAULT_STACK));
                            } else if (annotation.annotationType() == PeekFrom.class) {
                                parameterInfos.add(new ParameterInfo(ParameterKind.PEEK,
                                                                     ((PeekFrom) annotation).value()));
                            } else {
                                throw new MappingException(
                                    String.format(
                                        "Requested unknown parameter with unknown annotation: %s %s",
                                        annotation, type.getSimpleName()
                                    )
                                );
                            }
                        // If there are no annotations, throw an exception
                        } else {
                            throw new MappingException(
                                "Requested unknown parameter with no annotations: " + type.getSimpleName()
                            );
                        }
                    }
                }

                // Add a mapping into one of the categories
                if (method.isAnnotationPresent(Before.class)) {
                    beforeMappings.put(path, new ReflectedReactionBefore(object, method, stack, parameterInfos));
                } else if (method.isAnnotationPresent(At.class)) {
                    atMappings.put(path, new ReflectedReactionAt(object, method, stack, parameterInfos));
                } else {
                    afterMappings.put(path, new ReflectedReactionAfter(object, method, stack, parameterInfos));
                }
            } else {
                // TODO: warn about bogus method
            }
        }

        return Mappings.combineMappings(beforeMappings, atMappings, afterMappings);
    }

    private static class AbstractReflectedReaction {
        private final Object object;
        private final Method method;
        private final Optional<String> stack;
        private final List<ParameterInfo> parameterInfos;
        private final Object[] arguments;

        private AbstractReflectedReaction(Object object, Method method, Optional<String> stack,
                                          List<ParameterInfo> parameterInfos) {
            this.object = object;
            this.method = method;
            this.stack = stack;
            this.parameterInfos = parameterInfos;
            this.arguments = new Object[parameterInfos.size()];
        }

        protected void fillArguments(Stacks stacks, AttributesContext context, Optional<String> body) {
            for (int i = 0; i < parameterInfos.size(); ++i) {
                ParameterInfo parameterInfo = parameterInfos.get(i);
                switch (parameterInfo.kind) {
                    case POP: {
                        Object object = stacks.pop(parameterInfo.argument.get());
                        arguments[i] = object;
                        break;
                    }
                    case PEEK: {
                        Object object = stacks.peek(parameterInfo.argument.get());
                        arguments[i] = object;
                        break;
                    }
                    case BODY: {
                        arguments[i] = body.get();
                        break;
                    }
                    case CONTEXT: {
                        arguments[i] = context;
                        break;
                    }
                    default:
                        throw new IllegalStateException("Invalid enumeration value: " + parameterInfo.kind);
                }
            }
        }

        protected void invokeMethod(Stacks stacks, AttributesContext context, Optional<String> body) {
            fillArguments(stacks, context, body);
            Object result;
            try {
                result = method.invoke(object, arguments);
            } catch (Exception e) {
                throw new DevourerException("Error invoking reaction method", e);
            }
            if (stack.isPresent()) {
                stacks.push(stack.get(), result);
            }
        }
    }

    private static class ReflectedReactionBefore extends AbstractReflectedReaction implements ReactionBefore {
        private ReflectedReactionBefore(Object object, Method method, Optional<String> stack,
                                       List<ParameterInfo> parameterInfos) {
            super(object, method, stack, parameterInfos);
        }

        @Override
        public void react(Stacks stacks, AttributesContext context) {
            invokeMethod(stacks, context, Optional.<String>absent());
        }
    }

    private static class ReflectedReactionAt extends AbstractReflectedReaction implements ReactionAt {
        private ReflectedReactionAt(Object object, Method method, Optional<String> stack,
                                    List<ParameterInfo> parameterInfos) {
            super(object, method, stack, parameterInfos);
        }

        @Override
        public void react(Stacks stacks, AttributesContext context, String body) {
            invokeMethod(stacks, context, Optional.of(body));
        }
    }

    private static class ReflectedReactionAfter extends AbstractReflectedReaction implements ReactionAfter {
        private ReflectedReactionAfter(Object object, Method method, Optional<String> stack,
                                       List<ParameterInfo> parameterInfos) {
            super(object, method, stack, parameterInfos);
        }

        @Override
        public void react(Stacks stacks, AttributesContext context) {
            invokeMethod(stacks, context, Optional.<String>absent());
        }
    }

    private static enum ParameterKind {
        POP, PEEK, BODY, CONTEXT
    }

    private static class ParameterInfo {
        private final ParameterKind kind;
        private final Optional<String> argument;

        private ParameterInfo(ParameterKind kind, String argument) {
            this.kind = kind;
            this.argument = Optional.of(argument);
        }

        private ParameterInfo(ParameterKind kind) {
            this.kind = kind;
            this.argument = Optional.absent();
        }
    }
 }
