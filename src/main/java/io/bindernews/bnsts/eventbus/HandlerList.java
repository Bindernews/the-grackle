package io.bindernews.bnsts.eventbus;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.stream.Stream;

public class HandlerList<H> implements IHandlerList<H> {
    protected final MethodHandle hAccept;
    protected final ArrayList<Entry> handlers;

    public HandlerList(MethodHandle accept) {
        hAccept = accept;
        handlers = new ArrayList<>();
    }

    public HandlerList(Class<H> clz, String methodName) {
        this(EventBus.findHandle(clz, methodName));
    }

    @Override
    public void on(int priority, H handler) {
        val entry = new Entry(handler, priority, hAccept.bindTo(handler));
        EventBus.addOrdered(handlers, entry);
    }

    @Override
    public void off(H handler) {
        handlers.removeIf(e -> e.handler == handler);
    }

    @Override
    public void once(H handler) {
        val entry = new Entry(handler, 9999, hAccept.bindTo(handler));
        entry.once = true;
        EventBus.addOrdered(handlers, entry);
    }

    /** {@inheritDoc} */
    @Override @SuppressWarnings("unchecked")
    public @NotNull Stream<H> getHandlers() {
        return handlers.stream().map(e -> (H) e.handler);
    }

    @SneakyThrows
    public void emit(Object... args) {
        boolean anyOnce = false;
        for (val h : handlers) {
            h.hBound.invokeWithArguments(args);
            anyOnce = anyOnce || h.once;
        }
        if (anyOnce) {
            handlers.removeIf(h -> h.once);
        }
    }

    @Data
    protected static class Entry implements Comparable<Entry> {
        final Object handler;
        final int priority;
        /** Method handle bound with this object as the handler. */
        final MethodHandle hBound;
        protected boolean once = false;

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
