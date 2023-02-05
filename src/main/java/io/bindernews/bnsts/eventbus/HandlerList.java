package io.bindernews.bnsts.eventbus;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class HandlerList<H> implements IHandlerList<H> {
    protected final ArrayList<Entry> handlers;
    protected final ArrayList<Entry> removeList;
    protected final ArrayList<Entry> addList;

    /**
     * Tracks recursive calls to callEach so that we don't try to modify
     * {@code handlers} while iterating through it.
     */
    protected int locked = 0;

    public HandlerList() {
        handlers = new ArrayList<>();
        removeList = new ArrayList<>();
        addList = new ArrayList<>();
    }

    @Override
    public void on(int priority, H handler) {
        Entry entry = new Entry(handler, priority);
        if (locked == 0) {
            IHandlerList.addOrdered(handlers, entry);
        } else {
            addList.add(entry);
        }
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

    /** {@inheritDoc} */
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
    public void use(Consumer<Stream<H>> action) {
        try {
            locked++;
            action.accept(getHandlers());
        } finally {
            locked--;
            postProcess();
        }
    }

    protected void postProcess() {
        if (!removeList.isEmpty()) {
            handlers.removeAll(removeList);
            removeList.clear();
        }
        if (!addList.isEmpty()) {
            for (Entry e : addList) {
                IHandlerList.addOrdered(handlers, e);
            }
            addList.clear();
        }
    }

    protected static class Entry implements Comparable<Entry> {
        final Object handler;
        final int priority;

        public Entry(Object handler, int priority) {
            this.handler = handler;
            this.priority = priority;
        }

        @Override
        public int compareTo(@NotNull Entry o) {
            int d = priority - o.priority;
            if (d != 0) {
                return d;
            }
            d = System.identityHashCode(handler) - System.identityHashCode(o.handler);
            return d;
        }

        public Object getHandler() {
            return this.handler;
        }

        public int getPriority() {
            return this.priority;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof HandlerList.Entry)) return false;
            final Entry other = (Entry) o;
            return Objects.equals(this.getHandler(), other.getHandler())
                    && this.getPriority() == other.getPriority();
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $handler = this.getHandler();
            result = result * PRIME + ($handler == null ? 43 : $handler.hashCode());
            result = result * PRIME + this.getPriority();
            return result;
        }

        public String toString() {
            return "HandlerList.Entry(handler=" + this.getHandler() + ", priority=" + this.getPriority() + ")";
        }
    }
}
