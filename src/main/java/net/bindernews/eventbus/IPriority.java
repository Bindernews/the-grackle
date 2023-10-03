package net.bindernews.eventbus;

import org.jetbrains.annotations.NotNull;

/**
 * Interface with default implementations allowing functional interfaces to easily add a "priority" feature.
 */
public interface IPriority<T> extends Comparable<T> {

    default int getPriority() {
        return 0;
    }

    @Override
    default int compareTo(@NotNull T o) {
        int d = getPriority() - ((IPriority<?>)o).getPriority();
        if (d != 0) {
            return d;
        } else {
            return hashCode() - o.hashCode();
        }
    }
}
