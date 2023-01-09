package io.bindernews.bnsts.eventbus;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.Consumer;

public class EventBus {

    private final HashMap<Class<?>, IHandlerList<?>> handlers = new HashMap<>();
    /**
     * For single-argument handlers with unique types we provide emitEvent,
     * so you don't have to explicitly specify the handler type every time.
     */
    private final HashMap<Class<?>, Class<?>> eventReverseMap = new HashMap<>();

    /**
     * Register a new event handler type.
     * @param handlerType A {@link FunctionalInterface}, similar to {@link Consumer}
     * @param methodName Name of the functional method (e.g. "accept")
     */
    public void register(Class<?> handlerType, String methodName) {
        val handle = findHandle(handlerType, methodName);
        handlers.put(handlerType, new HandlerList<>(handle));
    }

    public <T> void registerConsumer(Class<? extends Consumer<T>> handlerType) {
        val eventType = reifyConsumer(handlerType);
        if (eventReverseMap.containsKey(eventType)) {
            throw new EventBusException("duplicate event class " + eventType.getName());
        }
        eventReverseMap.put(eventType, handlerType);
        handlers.put(handlerType, new EventEmit<>());
    }

    /**
     * Check if a handler type is registered.
     * @param handlerType The handler interface type
     * @return true if the handler type is registered
     * @param <T> Handler type
     */
    public <T> boolean has(Class<T> handlerType) {
        return handlers.containsKey(handlerType);
    }

    /**
     * Returns the event emitter object for the given handler type, or
     * throws an exception if the handler type has not been registered.
     * <br/>
     * Sometimes the convenience methods may fail, in which case calling
     * {@code get()} and then calling methods directly on the handler will always work.
     *
     * @param clz Handler type class
     * @param <T> Handler type
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public <T> IHandlerList<T> get(Class<T> clz) {
        val ee = handlers.get(clz);
        if (ee == null) {
            throw new EventBusException(clz.getName() + " is not registered");
        }
        return (IHandlerList<T>) ee;
    }

    public <T> void on(T handler) {
        on(0, handler);
    }

    public <T> void on(int priority, T handler) {
        get(reify(handler)).on(priority, handler);
    }

    public <T> void off(T handler) {
        get(reify(handler)).off(handler);
    }

    public <T> void emit(Class<T> handlerType, Object... args) {
        get(handlerType).emit(args);
    }

    public <T> void emitEvent(T event) {
        val handlerType = eventReverseMap.get(event.getClass());
        if (handlerType == null) {
            throw new EventBusException("unknown event type " + event.getClass().getName());
        }
        get(handlerType).emit(event);
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> reify(T handler) {
        if (handler instanceof SerializedLambda) {
            val clzName = ((SerializedLambda) handler).getFunctionalInterfaceClass();
            try {
                return (Class<T>) Class.forName(clzName);
            } catch (ClassNotFoundException e) {
                throw new EventBusException("unable to reify " + handler, e);
            }
        }
        val interfaces = handler.getClass().getInterfaces();
        for (val inter : interfaces) {
            if (handlers.containsKey(inter)) {
                return (Class<T>) inter;
            }
        }
        throw new EventBusException("unable to reify " + handler);
    }

    protected static Class<?> reifyConsumer(Class<? extends Consumer<?>> type) {
        val inters = type.getGenericInterfaces();
        for (val inter : inters) {
            if (inter instanceof ParameterizedType) {
                val pt = (ParameterizedType) inter;
                if (pt.getRawType() == Consumer.class) {
                    val type0 = pt.getActualTypeArguments()[0];
                    if (type0 instanceof Class<?>) {
                        return (Class<?>) type0;
                    }
                }
            }
        }
        throw new EventBusException("unable to determine argument type for " + type.getName());
    }

    protected static MethodHandle findHandle(Class<?> clz, String name) {
        val ERR_MULTIPLE_ACCEPT = "%s has multiple methods named '%s'";
        val ERR_NO_ACCEPT = "%s has no method named '%s'";

        val methodRefs = Arrays.stream(clz.getMethods())
                .filter(m -> m.getName().equals(name))
                .toArray(Method[]::new);
        if (methodRefs.length > 1) {
            throw new EventBusException(String.format(ERR_MULTIPLE_ACCEPT, clz.getName(), name));
        }
        if (methodRefs.length == 0) {
            throw new EventBusException(String.format(ERR_NO_ACCEPT, clz.getName(), name));
        }
        try {
            return MethodHandles.lookup().unreflect(methodRefs[0]);
        } catch (IllegalAccessException e) {
            throw new EventBusException(e);
        }
    }


    /**
     * If this handler type accepts only one argument, returns that class.
     * <br/>
     * This is the "event type" and is used as a convenience.
     */
    @Nullable
    protected static Class<?> getEventClass(MethodHandle handle) {
        val params = handle.type().parameterArray();
        if (params.length == 2) {
            return params[1];
        } else {
            return null;
        }
    }

    public static <E extends Comparable<E>> boolean addOrdered(ArrayList<E> list, E elem) {
        int pos = Collections.binarySearch(list, elem);
        if (pos < 0) {
            list.add(-(pos + 1), elem);
            return true;
        }
        return false;
    }
}
