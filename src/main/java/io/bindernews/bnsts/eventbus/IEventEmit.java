package io.bindernews.bnsts.eventbus;

import java.util.function.Consumer;

/**
 * Specialization of {@link IHandlerList} that takes a single argument.
 * @param <T> Event type
 */
public interface IEventEmit<T> extends IHandlerList<Consumer<T>> {
    void emit(T event);

    @Override
    @SuppressWarnings("unchecked")
    default void emit(Object... args) {
        if (args.length != 1) {
            throw new RuntimeException("IEventEmit only takes ONE argument");
        }
        emit((T) args[0]);
    }
}
