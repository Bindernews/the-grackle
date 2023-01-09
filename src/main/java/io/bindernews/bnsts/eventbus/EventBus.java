package io.bindernews.bnsts.eventbus;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.*;
import java.util.*;
import java.util.function.Consumer;

public class EventBus {

    private final HashMap<Class<?>, HandlerList> handlers = new HashMap<>();

    /**
     * Register a new event handler type.
     * @param handlerType A {@link FunctionalInterface}, similar to {@link Consumer}
     * @param methodName Name of the functional method (e.g. "accept")
     */
    public void register(Class<?> handlerType, String methodName) {
        handlers.put(handlerType, new HandlerList(handlerType, methodName));
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
    public <T> IEventEmit<T> get(Class<T> clz) {
        val ee = handlers.get(clz);
        if (ee == null) {
            throw new EventBusException(clz.getName() + " is not registered");
        }
        return (IEventEmit<T>) ee;
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
//        val inters = handler.getClass().getGenericInterfaces();
//        for (val inter : inters) {
//            if (inter instanceof ParameterizedType) {
//                val pt = (ParameterizedType) inter;
//                if (pt.getRawType() == Consumer.class) {
//                    val type0 = pt.getActualTypeArguments()[0];
//                    if (type0 instanceof Class<?>) {
//                        return (Class<T>) type0;
//                    }
//                }
//            }
//        }
        throw new EventBusException("unable to reify " + handler);
    }


    protected static class HandlerList implements IEventEmit<Object> {
        static final String ERR_MULTIPLE_ACCEPT = "%s has multiple methods named '%s'";
        static final String ERR_NO_ACCEPT = "%s has no method named '%s'";

        final Class<?> handlerClass;
        final MethodHandle hAccept;
        final ArrayList<Entry> handlers;
        /** Array of arguments, re-used for every call to emit to reduce GC churn */
        private final Object[] argsArray;
        /** If we're currently emitting, we can't re-use argsArray, special recursive handling */
        private boolean emitting = false;

        public HandlerList(Class<?> clz, String acceptName) {
            handlerClass = clz;
            val methodRef = Arrays.stream(handlerClass.getMethods())
                    .filter(m -> m.getName().equals(acceptName))
                    .reduce(null, (ident, m) -> {
                        if (ident != null) {
                            throw new EventBusException(
                                    String.format(ERR_MULTIPLE_ACCEPT, clz.getName(), acceptName));
                        }
                        return m;
                    });
            if (methodRef == null) {
                throw new EventBusException(
                        String.format(ERR_NO_ACCEPT, clz.getName(), acceptName));
            }
            try {
                hAccept = MethodHandles.lookup().unreflect(methodRef);
            } catch (IllegalAccessException e) {
                throw new EventBusException(e);
            }

            handlers = new ArrayList<>();
            argsArray = new Object[hAccept.type().parameterCount()];
        }

        public void on(int priority, Object handler) {
            val entry = new Entry(handler, priority);
            int pos = Collections.binarySearch(handlers, entry);
            if (pos < 0) {
                handlers.add(-(pos + 1), entry);
            }
        }

        public void off(Object handler) {
            handlers.removeIf(e -> e.handler == handler);
        }

        @SneakyThrows
        public void emit(Object... args) {
            if (!emitting) {
                // Normal case, we can re-use argsArray
                try {
                    emitting = true;
                    System.arraycopy(args, 0, argsArray, 1, argsArray.length - 1);
                    for (val e : handlers) {
                        argsArray[0] = e.handler;
                        hAccept.invokeWithArguments(argsArray);
                    }
                } finally {
                    emitting = false;
                }
            } else {
                // Special recursive case, cannot re-use argsArray
                val args2 = new Object[args.length + 1];
                System.arraycopy(args, 0, args2, 1, args.length);
                for (val e : handlers) {
                    args2[0] = e.handler;
                    hAccept.invokeWithArguments(args2);
                }
            }
        }
    }

    @Data
    protected static class Entry implements Comparable<Entry> {
        final Object handler;
        final int priority;

        @Override
        public int compareTo(@NotNull Entry o) {
            int d = priority - o.priority;
            if (d != 0) {
                return d;
            }
            d = System.identityHashCode(handler) - System.identityHashCode(o.handler);
            return d;
        }
    }
}
