package io.bindernews.bnsts.eventbus;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class HandlerList<H> implements IHandlerList<H> {
    protected final ArrayList<Entry> handlers;
    protected final ArrayList<Entry> removeList;

    /**
     * Tracks recursive calls to callEach so that we don't try to modify
     * {@code handlers} while iterating through it.
     */
    protected int locked = 0;

    public HandlerList() {
        handlers = new ArrayList<>();
        removeList = new ArrayList<>();
    }

    @Override
    public void on(int priority, H handler) {
        Entry entry = new Entry(handler, priority);
        IHandlerList.addOrdered(handlers, entry);
    }

    @Override
    public void off(H handler) {
        if (locked == 0) {
            handlers.removeIf(e -> e.handler == handler);
        } else {
            offLater(handler);
        }
    }

    public void offLater(H handler) {
        Entry toRemove = null;
        for (Entry e : handlers) {
            if (e.handler == handler) {
                toRemove = e;
                break;
            }
        }
        if (toRemove != null) {
            removeList.add(toRemove);
        }
    }

    @Override
    public void once(H handler) {
        Entry entry = new Entry(handler, 9999);
        IHandlerList.addOrdered(handlers, entry);
        removeList.add(entry);
    }

    /** {@inheritDoc} */
    @Override @SuppressWarnings("unchecked")
    public @NotNull Stream<H> getHandlers() {
        return handlers.stream().map(e -> (H) e.handler);
    }

    /** {@inheritDoc} */
    @Override @SuppressWarnings("unchecked")
    public void callEach(Consumer<H> action) {
        locked++;
        for (Entry h : handlers) {
            action.accept((H) h.handler);
        }
        if (!removeList.isEmpty()) {
            handlers.removeAll(removeList);
            removeList.clear();
        }
        locked--;
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
