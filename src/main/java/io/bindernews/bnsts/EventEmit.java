package io.bindernews.bnsts;

import lombok.Data;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.Consumer;

public class EventEmit<T> {
    public static final int DEFAULT_PRIORITY = 5;

    private final TreeSet<ListenerEntry<T>> handlers = new TreeSet<>();

    public void listen(Consumer<T> handler) {
        listen(handler, DEFAULT_PRIORITY);
    }

    public void listen(Consumer<T> handler, int priority) {
        handlers.add(new ListenerEntry<>(handler, priority));
    }

    public void unlisten(Consumer<T> handler) {
        handlers.removeIf(x -> x.handler == handler);
    }

    public void publish(T event) {
        for (val h : handlers) {
            h.handler.accept(event);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> reify(Consumer<T> handler) {
        val inters = handler.getClass().getGenericInterfaces();
        for (val inter : inters) {
            if (inter instanceof ParameterizedType) {
                val pt = (ParameterizedType) inter;
                if (pt.getRawType() == Consumer.class) {
                    val type0 = pt.getActualTypeArguments()[0];
                    if (type0 instanceof Class<?>) {
                        return (Class<T>) type0;
                    }
                }
            }
        }
        throw new RuntimeException("unable to reify " + handler);
    }

    @Data
    protected static class ListenerEntry<T> implements Comparable<ListenerEntry<T>> {
        final Consumer<T> handler;
        final int priority;

        @Override
        public int compareTo(@NotNull EventEmit.ListenerEntry o) {
            int d = priority - o.priority;
            if (d != 0) {
                return d;
            }
            d = System.identityHashCode(this) - System.identityHashCode(o);
            return d;
        }
    }
}
