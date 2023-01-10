package io.bindernews.bnsts.eventbus;

import lombok.Data;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
public class EventEmit<T> implements IEventEmit<T> {
    public static final int DEFAULT_PRIORITY = 0;

    private final ArrayList<Entry<T>> handlers = new ArrayList<>();

    /** {@inheritDoc} */
    @Override
    public void on(Consumer<T> handler) {
        on(DEFAULT_PRIORITY, handler);
    }

    /** {@inheritDoc} */
    @Override
    public void on(int priority, Consumer<T> handler) {
        val entry = new Entry<>(handler, priority);
        EventBus.addOrdered(handlers, entry);
    }

    /** {@inheritDoc} */
    @Override
    public void off(Consumer<T> handler) {
        handlers.removeIf(x -> x.handler == handler);
    }

    /** {@inheritDoc} */
    @Override
    public void emit(T event) {
        boolean anyOnce = false;
        for (val h : handlers) {
            h.handler.accept(event);
            anyOnce = anyOnce || h.once;
        }
        if (anyOnce) {
            handlers.removeIf(h -> h.once);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void once(Consumer<T> handler) {
        val entry = new Entry<>(handler, 9999);
        entry.once = true;
        EventBus.addOrdered(handlers, entry);
    }

    @Data
    protected static class Entry<T> implements Comparable<Entry<T>> {
        final Consumer<T> handler;
        final int priority;
        boolean once = false;

        @Override
        public int compareTo(@NotNull EventEmit.Entry o) {
            int d = priority - o.priority;
            if (d != 0) {
                return d;
            }
            d = System.identityHashCode(handler) - System.identityHashCode(o.handler);
            return d;
        }
    }
}
