package net.bindernews.eventbus;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

class Entry<H> implements Comparable<Entry<H>>, IPriority<Entry<H>> {
    final H handler;
    final int priority;

    public Entry(H handler, int priority) {
        this.handler = handler;
        this.priority = priority;
    }

    public Entry(H handler) {
        this(handler, 0);
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

    public H getHandler() {
        return this.handler;
    }

    public int getPriority() {
        return this.priority;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Entry)) return false;
        @SuppressWarnings("unchecked")
        final Entry<Object> other = (Entry<Object>) o;
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
        return "Entry(handler=" + this.getHandler() + ", priority=" + this.getPriority() + ")";
    }
}
