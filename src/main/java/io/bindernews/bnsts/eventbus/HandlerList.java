package io.bindernews.bnsts.eventbus;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;

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

    public void on(int priority, H handler) {
        val entry = new Entry(handler, priority, hAccept.bindTo(handler));
        EventBus.addOrdered(handlers, entry);
    }

    public void off(Object handler) {
        handlers.removeIf(e -> e.handler == handler);
    }

    @SneakyThrows
    public void emit(Object... args) {
        for (val e : handlers) {
            e.hBound.invokeWithArguments(args);
        }
    }

    @Data
    protected static class Entry implements Comparable<Entry> {
        final Object handler;
        final int priority;
        /** Method handle bound with this object as the handler. */
        final MethodHandle hBound;

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
