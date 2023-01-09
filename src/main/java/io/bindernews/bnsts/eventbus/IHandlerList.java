package io.bindernews.bnsts.eventbus;

/**
 * The handler list for a specific type of event handler.
 *
 * @param <T> A {@link FunctionalInterface} that encapsulates handlers
 */
public interface IHandlerList<T> {
    default void on(T handler) {
        on(0, handler);
    }

    void on(int priority, T handler);

    void off(T handler);

    void emit(Object... args);
}
