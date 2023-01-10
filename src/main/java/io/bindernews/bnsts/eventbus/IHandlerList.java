package io.bindernews.bnsts.eventbus;

/**
 * The handler list for a specific type of event handler.
 *
 * @param <T> A {@link FunctionalInterface} that encapsulates handlers
 */
public interface IHandlerList<T> {

    /**
     * Add a handler with the default priority.
     * @param handler Event handler
     */
    default void on(T handler) {
        on(0, handler);
    }

    /**
     * Add a handler with a specific priority.
     *
     * @param priority Priority, lower values are called earlier
     * @param handler  Event handler
     */
    void on(int priority, T handler);

    /**
     * Remove the handler, does nothing if the handler is not registered.
     *
     * @param handler Event handler
     */
    void off(T handler);

    /**
     * Adds a handler to the end of the handler list, removing it
     * after it has been called once.
     * @param handler Event handler
     */
    void once(T handler);

    void emit(Object... args);
}
