package io.bindernews.bnsts;

import lombok.Data;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

/**
 * <p>
 * Somewhat similar to NodeJS EventEmitter, each instance of {@link EventEmit} only has
 * one type of event, for simplicity’s sake.
 * </p>
 * <br/>
 * <p>
 * Some event types are "read-only", but others may be mutable (e.g. events which can be cancelled).
 * To accommodate this, handlers may be added with a priority. Handlers are called from lowest to
 * highest priority (e.g. a handler with -5 priority will be called before a handler at 0, or 1).
 * <br/>
 * Handlers with the same priority will be called in effectively random order.
 * </p>
 * <p>
 * Recommended usage is to register handlers up-front once. Adding a large number of handlers,
 * or frequently adding and removing handlers, may degrade performance.
 * </p>
 *
 * @param <T> The event type
 */
public class EventEmit<T> {
    public static final int DEFAULT_PRIORITY = 0;

    private final ArrayList<ListenerEntry<T>> handlers = new ArrayList<>();

    /**
     * Add a handler with the default priority.
     * @param handler Event handler
     */
    public void on(Consumer<T> handler) {
        on(DEFAULT_PRIORITY, handler);
    }

    /**
     * Add a handler with a specific priority.
     *
     * @param priority Priority, lower values are called earlier
     * @param handler  Event handler
     */
    public void on(int priority, Consumer<T> handler) {
        val entry = new ListenerEntry<>(handler, priority);
        int pos = Collections.binarySearch(handlers, entry);
        if (pos < 0) {
            handlers.add(-(pos + 1), entry);
        }
    }

    /**
     * Remove the handler, does nothing if the handler is not registered.
     *
     * @param handler Event handler
     */
    public void off(Consumer<T> handler) {
        handlers.removeIf(x -> x.handler == handler);
    }

    public void emit(T event) {
        for (val h : handlers) {
            h.handler.accept(event);
        }
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
            d = System.identityHashCode(handler) - System.identityHashCode(o.handler);
            return d;
        }
    }
}
