package org.bitbucket.googolplex.devourer.configuration.annotated.internal;

import com.google.common.base.Optional;
import org.bitbucket.googolplex.devourer.contexts.AttributesContext;
import org.bitbucket.googolplex.devourer.exceptions.DevourerException;
import org.bitbucket.googolplex.devourer.stacks.Stacks;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Base class for actions-delegates to annotated configuration object. Contains all reflection machinery which
 * will constructs method arguments and call it.
 */
public class AbstractReflectedAction {
    private final ThreadLocal<Object[]> arguments;
    private final Object object;
    private final Method method;
    private final Optional<String> stack;
    private final List<ParameterInfo> parameterInfos;

    protected AbstractReflectedAction(Object object, Method method, Optional<String> stack,
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

    private void fillArguments(Stacks stacks, AttributesContext context, Optional<String> body) {
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
                case POP_LIST:
                    arguments.get()[i] = stacks.get(parameterInfo.argument.get()).popList();
                    break;
                case PEEK_LIST:
                    arguments.get()[i] = stacks.get(parameterInfo.argument.get()).peekList();
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
                case ATTRIBUTE:
                    arguments.get()[i] = context.attribute(parameterInfo.argument.get()).get();
                    break;
                case TRY_ATTRIBUTE:
                    arguments.get()[i] = context.attribute(parameterInfo.argument.get());
                    break;
                default:
                    throw new DevourerException("Invalid enumeration value: " + parameterInfo.kind);
            }
        }
    }

    public void invokeMethod(Stacks stacks, AttributesContext context, Optional<String> body) {
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
