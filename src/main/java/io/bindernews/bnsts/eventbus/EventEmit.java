package io.bindernews.bnsts.eventbus;

import java.util.function.Consumer;

/**
 * <p>
 * Somewhat similar to NodeJS EventEmitter, each instance of {@link EventEmit} only has
 * one type of event, for simplicity’s sake.
 * </p>
 * <br/>
 * <p>
 * Some event types are "read-only", but others may be mutable (e.g. events which can be cancelled).
 * To accommodate this, handlers may be added with a priority. Handlers are called from lowest to
 * highest priority (e.g. a handler with -5 priority will be called before a handler at 0, or 1).
 * <br/>
 * Handlers with the same priority will be called in effectively random order.
 * </p>
 * <p>
 * Recommended usage is to register handlers up-front once. Adding a large number of handlers,
 * or frequently adding and removing handlers, may degrade performance.
 * </p>
 *
 * @param <T> The event type
 */
public class EventEmit<T> extends HandlerList<Consumer<T>> implements IEventEmit<T> {
    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public void emit(T event) {
        try {
            locked++;
            for (Entry e : handlers) {
                ((Consumer<T>) e.handler).accept(event);
            }
        } finally {
            locked--;
            postProcess();
        }
    }
}
