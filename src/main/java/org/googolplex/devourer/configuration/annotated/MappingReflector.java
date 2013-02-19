package org.googolplex.devourer.configuration.annotated;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.googolplex.devourer.Constants;
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
import org.googolplex.devourer.paths.Path;
import org.googolplex.devourer.paths.PathMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Date: 19.02.13
 * Time: 17:02
 */
public class MappingReflector {
    public Map<Path, PathMapping> collectMappings(Object object) {
        Preconditions.checkNotNull(object, "Object is null");

        Class<?> clazz = object.getClass();

        ListMultimap<Path, ReactionBefore> beforeMappings = ArrayListMultimap.create();
        ListMultimap<Path, ReactionAfter> afterMappings = ArrayListMultimap.create();
        ListMultimap<Path, ReactionAt> atMappings = ArrayListMultimap.create();

        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(Before.class) ||
                method.isAnnotationPresent(At.class) ||
                method.isAnnotationPresent(After.class)) {

                String stack = Constants.Stacks.MAIN_STACK;
                if (method.isAnnotationPresent(PushTo.class)) {
                    stack = method.getAnnotation(PushTo.class).value();
                }

                String route;
                if (method.isAnnotationPresent(Before.class)) {
                    route = method.getAnnotation(Before.class).value();
                } else if (method.isAnnotationPresent(At.class)) {
                    route = method.getAnnotation(At.class).value();
                } else {
                    route = method.getAnnotation(After.class).value();
                }

                List<ParameterInfo> parameterInfos = new ArrayList<ParameterInfo>();
                Class<?>[] parameterTypes = method.getParameterTypes();
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                for (int i = 0; i < parameterTypes.length; ++i) {
                    Class<?> type = parameterTypes[i];
                    Annotation[] annotations = parameterAnnotations[i];
                    if (type == AttributesContext.class || type == ElementContext.class) {
                        parameterInfos.add(new ParameterInfo(ParameterKind.CONTEXT));
                    } else if (type == String.class) {
                        if (!method.isAnnotationPresent(At.class)) {
                            throw new IllegalStateException("Requested body not in @At method");
                        }
                        parameterInfos.add(new ParameterInfo(ParameterKind.BODY));
                    } else {
                        if (annotations.length > 0) {
                            Annotation annotation = annotations[0];
                            if (annotation.annotationType() == Pop.class) {
                                parameterInfos.add(new ParameterInfo(ParameterKind.POP,
                                                                     Constants.Stacks.MAIN_STACK));
                            } else if (annotation.annotationType() == PopFrom.class) {
                                parameterInfos.add(new ParameterInfo(ParameterKind.POP,
                                                                     ((PopFrom) annotation).value()));
                            } else if (annotation.annotationType() == Peek.class) {
                                parameterInfos.add(new ParameterInfo(ParameterKind.PEEK,
                                                                     Constants.Stacks.MAIN_STACK));
                            } else if (annotation.annotationType() == PeekFrom.class) {
                                parameterInfos.add(new ParameterInfo(ParameterKind.PEEK,
                                                                     Constants.Stacks.MAIN_STACK));
                            } else {
                                throw new IllegalStateException(
                                    String.format(
                                        "Requested unknown parameter with unknown annotation: %s %s",
                                        annotation, type.getSimpleName()
                                    )
                                );
                            }
                        } else {
                            throw new IllegalStateException(
                                "Requested unknown parameter with no annotations: " + type.getSimpleName()
                            );
                        }
                    }
                }
            }
        }

        return null;
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
