package net.bindernews.eventbus;

import java.util.function.Consumer;

/**
 * Specialization of {@link IHandlerList} that takes a single argument.
 * @param <T> Event type
 */
public interface IEventEmit<T> extends IHandlerList<Consumer<T>> {
    void emit(T event);
}
