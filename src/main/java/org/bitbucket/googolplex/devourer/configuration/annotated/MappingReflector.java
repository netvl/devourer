/*******************************************************************************
 * Copyright 2013 Vladimir Matveev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.bitbucket.googolplex.devourer.configuration.annotated;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.bitbucket.googolplex.devourer.paths.SimplePath;
import org.bitbucket.googolplex.devourer.stacks.Stacks;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAt;
import org.bitbucket.googolplex.devourer.configuration.annotated.annotations.After;
import org.bitbucket.googolplex.devourer.configuration.annotated.annotations.At;
import org.bitbucket.googolplex.devourer.configuration.annotated.annotations.Before;
import org.bitbucket.googolplex.devourer.configuration.annotated.annotations.Peek;
import org.bitbucket.googolplex.devourer.configuration.annotated.annotations.PeekFrom;
import org.bitbucket.googolplex.devourer.configuration.annotated.annotations.Pop;
import org.bitbucket.googolplex.devourer.configuration.annotated.annotations.PopFrom;
import org.bitbucket.googolplex.devourer.configuration.annotated.annotations.PushTo;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionAfter;
import org.bitbucket.googolplex.devourer.configuration.actions.ActionBefore;
import org.bitbucket.googolplex.devourer.contexts.AttributesContext;
import org.bitbucket.googolplex.devourer.contexts.ElementContext;
import org.bitbucket.googolplex.devourer.exceptions.DevourerException;
import org.bitbucket.googolplex.devourer.exceptions.MappingException;
import org.bitbucket.googolplex.devourer.paths.Mappings;
import org.bitbucket.googolplex.devourer.paths.PathMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link MappingReflector} class supports creation of mappings from a class annotated in a special way.
 * The syntax and overall view of such class is heavily inspired by Spring controllers configuration.
 * Basic idea of such configuration is that each method of this class considered to be an action which Devourer
 * should perform during parsing, much like full-fledged actions (implementations of {@code Reaction*} interfaces),
 * which are configured in modular configuration (see
 * {@link org.bitbucket.googolplex.devourer.configuration.modular.AbstractMappingModule}). The actions are configured
 * using annotations metadata.
 *
 * <p>A class of supplied object should contain methods annotated either with {@link Before}, {@link At} or
 * {@link After} annotation, each of which designating either before-, at- or after-action. These annotations
 * accept mandatory string parameter, a path to the element inside XML document this method will handle.</p>
 *
 * <p>Each method optionally can return a value. Such method can optionally be annotated with {@link PushTo}
 * annotations, which accepts string argument, a stack name. If non-void method is not annotated with {@link PushTo},
 * it is considered as being annotated with {@code @PushTo(Stacks.DEFAULT_STACK}. The return value of
 * such method will be pushed onto the corresponding stack during parsing process. It is an error to return
 * instances of {@link AttributesContext}, {@link ElementContext} or {@link Stacks} interfaces.</p>
 *
 * <p>Each method can take variable number of arguments. Argument types and annotations are inspected in order
 * to find out which values to inject into them. If argument type is {@link AttributesContext} or
 * {@link ElementContext}, the element context will be injected into it. If argument type is {@link String},
 * textual body of the currently processed element will be injected into it (only applicable to at-actions).
 * If argument type is {@link Stacks}, the stacks object used in this processing will be injected into the
 * it. However, the need for manual injection of {@link Stacks} is alleviated by {@link PushTo} annotation
 * and {@link Peek} and {@link Pop} annotations, described in the next paragraph.</p>
 *
 * <p>If argument type is not {@link AttributesContext}, {@link ElementContext} or {@link Stacks},
 * then the parameter must be annotated with one of the following annotations: {@link Peek}, {@link PeekFrom},
 * {@link Pop}, {@link PopFrom}. {@link PeekFrom} and {@link PopFrom} accept a string parameter
 * which should be the name of the stack. {@code @Peek} is considered equals to {@code @PeekFrom(Stacks.DEFAULT_STACK)},
 * {@code @Pop} is considered equals to {@code @PopFrom(Stacks.DEFAULT_STACK}. These annotations allow implicit
 * manipulation of stacks object. Essentialy, such annotated fields will be injected with the object from the
 * top of the stack; if the field is annotated with {@code Pop*} annotation, the object being injected will
 * be removed from the stack; if the field is annotated with {@code Peek*} annotation, the object will be
 * retained on the top of the stack. Consequently, the number and the order of annotations matter.</p>
 *
 * <p>The arguments of the method are inspected in declaration order, and actions on the stacks object during the
 * processing are taken exactly in this order too. So, if you have e.g. the following declaration:
 * <pre>
 *    {@literal @}After("/some/node")
 *     public String someAction({@literal @}Pop String value1, {@literal @}Pop String value2,
 *                              {@literal @}Peek String value3, {@literal @}Peek String value4,
 *                              {@literal @}Pop value5) {
 *         // Actions
 *         return someResult;
 *     }
 * </pre>
 * then it will be equivalent to the following explicit action bound inside
 * {@link org.bitbucket.googolplex.devourer.configuration.modular.AbstractMappingModule}:
 * <pre>
 *     on("/some/node")
 *         .doAfter(new ActionAfter() {
 *            {@literal @}Override
 *             public void act(Stacks stacks, AttributeContext context) {
 *                 String value1 = stacks.pop();
 *                 String value2 = stacks.pop();
 *                 String value3 = stacks.peek();
 *                 String value4 = stacks.peek();
 *                 String value5 = stacks.pop();
 *                 // Actions
 *                 stacks.push(someResult);
 *             }
 *         });
 * </pre>
 * Compare the order of parameters declaration of the annotated method and definition order of local variables bound
 * to stack values directly inside the action code. The stack effect of actions will be exactly the same in both
 * examples.</p>
 *
 * <p>Stacks object is updated just before and right after calling the annotated method; this means that if
 * you inject {@link Stacks} object in the method, then it already will be updated according to parameters
 * annotations; if your method returns some value, the stacks object will be updated after method invocation.</p>
 *
 * <p>An order of actions is undefined in this variant of mapping configuration, that is, it is not guaranteed
 * that the actions will be invoked in some order during the parsing process. If you need well-defined ordering,
 * use modular configuration instead.</p>
 *
 * <p>Internally annotated mapping creates a number of standard {@code Reaction*} instances which
 * wrap configuration object methods invocation using reflection, which is significantly expensive,
 * especially when used in tight loops. An attempt is made to make annotated actions invocations as cheap is possible.
 * Configuration class inspection is done only at Devourer creation time. During the parsing only one reflection call
 * is made for each action invocation. However, if you need as much performance as you can get, consider using
 * modular configuration; it is done completely without reflection and should be as fast as it is possible at all.</p>
 *
 * <p>Single configuration object is used for all processings done by single Devourer, so is inadvisable to hold
 * any state inside configuration object because this will ruin Devourer's thread safety. Use only {@link Stacks}
 * object to hold the state of parsing process (either directly or via annotations and method return values);
 * this way the parsing process is guaranteed to be thread-safe.</p>
 *
 * <p>Despite that action objects generated during annotated configuration processing contain some internal mutable
 * state, it is wrapped into {@link ThreadLocal}, so it is not possible for different threads to interfere with
 * each other.</p>
 */
public class MappingReflector {
    public Map<SimplePath, PathMapping> collectMappings(Object object) {
        Preconditions.checkNotNull(object, "Object is null");

        // Extract object class
        Class<?> clazz = object.getClass();

        // Prepare temporary collections
        ListMultimap<SimplePath, ActionBefore> beforeMappings = ArrayListMultimap.create();
        ListMultimap<SimplePath, ActionAfter> afterMappings = ArrayListMultimap.create();
        ListMultimap<SimplePath, ActionAt> atMappings = ArrayListMultimap.create();

        // Loop through all available methods
        for (Method method : clazz.getMethods()) {
            // Only inspect a method annotated with @Before, @At or @After
            if (method.isAnnotationPresent(Before.class) ||
                method.isAnnotationPresent(At.class) ||
                method.isAnnotationPresent(After.class)) {

                // Check whether the method returns something and try to get stack name where to push
                Optional<String> stack = Optional.absent();
                if (method.getReturnType() != void.class) {
                    // Method cannot return stacks or element context
                    if (method.getReturnType() == Stacks.class ||
                        method.getReturnType() == ElementContext.class ||
                        method.getReturnType() == AttributesContext.class) {
                        throw new MappingException(
                            String.format(
                                "Invalid return type of method %s: %s",
                                method.getName(), method.getReturnType().getSimpleName()
                            )
                        );
                    }
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
                SimplePath path = SimplePath.fromString(route);

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

                    // Parameter is of stacks type1
                    } else if (type == Stacks.class) {
                        parameterInfos.add(new ParameterInfo(ParameterKind.STACKS));

                    // Parameter is of string type -- body
                    } else if (type == String.class &&
                               !anyPresent(annotations, Pop.class, PopFrom.class, Peek.class, PeekFrom.class)) {
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
                            ParameterKind kind;
                            String parameterStack;
                            if (annotation.annotationType() == Pop.class) {
                                parameterStack = Stacks.DEFAULT_STACK;
                                if (Optional.class.equals(type)) {
                                    kind = ParameterKind.TRY_POP;
                                } else {
                                    kind = ParameterKind.POP;
                                }
                            } else if (annotation.annotationType() == PopFrom.class) {
                                parameterStack = ((PopFrom) annotation).value();
                                if (Optional.class.equals(type)) {
                                    kind = ParameterKind.TRY_POP;
                                } else {
                                    kind = ParameterKind.POP;
                                }
                            } else if (annotation.annotationType() == Peek.class) {
                                parameterStack = Stacks.DEFAULT_STACK;
                                if (Optional.class.equals(type)) {
                                    kind = ParameterKind.TRY_PEEK;
                                } else {
                                    kind = ParameterKind.PEEK;
                                }
                            } else if (annotation.annotationType() == PeekFrom.class) {
                                parameterStack = ((PeekFrom) annotation).value();
                                if (Optional.class.equals(type)) {
                                    kind = ParameterKind.TRY_PEEK;
                                } else {
                                    kind = ParameterKind.PEEK;
                                }
                            } else {
                                throw new MappingException(
                                    String.format(
                                        "Requested unknown parameter with unknown annotation: %s %s",
                                        annotation, type.getSimpleName()
                                    )
                                );
                            }
                            parameterInfos.add(new ParameterInfo(kind, parameterStack));

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
                    beforeMappings.put(path, new ReflectedActionBefore(object, method, stack, parameterInfos));
                } else if (method.isAnnotationPresent(At.class)) {
                    atMappings.put(path, new ReflectedActionAt(object, method, stack, parameterInfos));
                } else {
                    afterMappings.put(path, new ReflectedActionAfter(object, method, stack, parameterInfos));
                }
            } else {
                // TODO: warn about bogus method
            }
        }

        return Mappings.combineMappings(beforeMappings, atMappings, afterMappings);
    }

    private boolean anyPresent(Annotation[] annotations, Class... what) {
        for (Annotation first : annotations) {
            for (Class second : what) {
                if (second.isAssignableFrom(first.getClass())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static class AbstractReflectedAction {
        private final ThreadLocal<Object[]> arguments;
        private final Object object;
        private final Method method;
        private final Optional<String> stack;
        private final List<ParameterInfo> parameterInfos;

        private AbstractReflectedAction(Object object, Method method, Optional<String> stack,
                                        final List<ParameterInfo> parameterInfos) {
            this.object = object;
            this.method = method;
            this.stack = stack;
            this.parameterInfos = parameterInfos;
            this.arguments = new ThreadLocal<Object[]>() {
                @Override
                protected Object[] initialValue() {
                    return new Object[parameterInfos.size()];
                }
            };
        }

        protected void fillArguments(Stacks stacks, AttributesContext context, Optional<String> body) {
            for (int i = 0; i < parameterInfos.size(); ++i) {
                ParameterInfo parameterInfo = parameterInfos.get(i);
                switch (parameterInfo.kind) {
                    case POP:
                        arguments.get()[i] = stacks.get(parameterInfo.argument.get()).pop();
                        break;
                    case TRY_POP:
                        arguments.get()[i] = stacks.get(parameterInfo.argument.get()).tryPop();
                        break;
                    case PEEK:
                        arguments.get()[i] = stacks.get(parameterInfo.argument.get()).peek();
                        break;
                    case TRY_PEEK:
                        arguments.get()[i] = stacks.get(parameterInfo.argument.get()).tryPeek();
                        break;
                    case BODY:
                        arguments.get()[i] = body.get();
                        break;
                    case CONTEXT:
                        arguments.get()[i] = context;
                        break;
                    case STACKS:
                        arguments.get()[i] = stacks;
                        break;
                    default:
                        throw new DevourerException("Invalid enumeration value: " + parameterInfo.kind);
                }
            }
        }

        protected void invokeMethod(Stacks stacks, AttributesContext context, Optional<String> body) {
            fillArguments(stacks, context, body);
            Object result;
            try {
                result = method.invoke(object, arguments.get());
            } catch (Exception e) {
                throw new DevourerException("Error invoking action method", e);
            }
            if (stack.isPresent()) {
                stacks.get(stack.get()).push(result);
            }
        }
    }

    private static class ReflectedActionBefore extends AbstractReflectedAction implements ActionBefore {
        private ReflectedActionBefore(Object object, Method method, Optional<String> stack,
                                      List<ParameterInfo> parameterInfos) {
            super(object, method, stack, parameterInfos);
        }

        @Override
        public void act(Stacks stacks, AttributesContext context) {
            invokeMethod(stacks, context, Optional.<String>absent());
        }
    }

    private static class ReflectedActionAt extends AbstractReflectedAction implements ActionAt {
        private ReflectedActionAt(Object object, Method method, Optional<String> stack,
                                  List<ParameterInfo> parameterInfos) {
            super(object, method, stack, parameterInfos);
        }

        @Override
        public void act(Stacks stacks, AttributesContext context, String body) {
            invokeMethod(stacks, context, Optional.of(body));
        }
    }

    private static class ReflectedActionAfter extends AbstractReflectedAction implements ActionAfter {
        private ReflectedActionAfter(Object object, Method method, Optional<String> stack,
                                     List<ParameterInfo> parameterInfos) {
            super(object, method, stack, parameterInfos);
        }

        @Override
        public void act(Stacks stacks, AttributesContext context) {
            invokeMethod(stacks, context, Optional.<String>absent());
        }
    }

    private static enum ParameterKind {
        POP, TRY_POP, PEEK, TRY_PEEK, BODY, CONTEXT, STACKS
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
