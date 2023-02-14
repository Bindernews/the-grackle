package net.bindernews.eventbus;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * An interface and implementations of the lazy-evaluation, or lazy initialization, pattern.
 * <p>
 * The implementation is based on Lombok's {@code @Getter(lazy=true)} annotation.
 * </p>
 * @param <T> Type of object to be lazily-loaded
 */
public interface Lazy<T> extends Supplier<T> {

    /**
     * Create a {@link Lazy} which is NOT thread-safe during initialization.
     * @param creator Lazy initializer
     * @return Lazy instance
     * @param <T> Lazy type
     */
    static <T> @NotNull Lazy<T> of(Supplier<T> creator) {
        return new SimpleLazy<>(creator);
    }

    /**
     * Create a {@link Lazy} which IS thread-safe during initialization.
     * @param creator Lazy initializer
     * @return Lazy instance
     * @param <T> Lazy type
     */
    static <T> @NotNull Lazy<T> ofSync(Supplier<T> creator) {
        return new SyncLazy<>(creator);
    }
}

class SimpleLazy<T> implements Lazy<T> {
    protected Object value = null;
    protected final Supplier<T> ctor;

    SimpleLazy(Supplier<T> ctor) {
        this.ctor = ctor;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get() {
        if (value == null) {
            Object actual = ctor.get();
            // If result is invalid, set to ctor
            value = actual == null ? ctor : actual;
        }
        return (T)(value == ctor ? null : value);
    }
}

class SyncLazy<T> implements Lazy<T> {
    protected final AtomicReference<Object> value = new AtomicReference<>();
    protected final Supplier<T> ctor;

    protected SyncLazy(Supplier<T> ctor) {
        this.ctor = ctor;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get() {
        Object v = value.get();
        if (v == null) {
            synchronized (value) {
                v = value.get();
                if (v == null) {
                    final T actual = ctor.get();
                    // If we set the value to itself, then ctor returned null
                    v = actual == null ? value : actual;
                    value.set(v);
                }
            }
        }
        // If v == value, return (T)null, else return (T)v
        return (T)(v == value ? null : v);
    }
}