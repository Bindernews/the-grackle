package net.bindernews.eventbus;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class HandlerList<H> implements IHandlerList<H> {
    protected final ArrayList<Entry<H>> handlers;
    protected final ArrayList<Entry<H>> removeList;
    protected final ArrayList<Entry<H>> addList;

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
        Entry<H> entry = new Entry(handler, priority);
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
        Entry<H> toRemove = null;
        for (Entry<H> e : handlers) {
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
        Entry<H> entry = new Entry<>(handler, 9999);
        IHandlerList.addOrdered(handlers, entry);
        removeList.add(entry);
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull Stream<H> getHandlers() {
        return handlers.stream().map(e -> e.handler);
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
            for (Entry<H> e : addList) {
                IHandlerList.addOrdered(handlers, e);
            }
            addList.clear();
        }
    }

}
