package io.bindernews.bnsts;

import java.util.concurrent.locks.*;
import java.util.function.Supplier;

public interface Lazy<T> extends Supplier<T> {
    static <T> Lazy<T> of(Supplier<T> creator) {
        return new Impl.SimpleLazy<>(creator);
    }

    static <T> Lazy<T> ofSync(Supplier<T> creator) {
        return new Impl.SyncLazy<>(creator);
    }
}

// Non-public classes can be in same file. This nicely
// hides the implementation and keeps it in the same file.
class Impl {
    static class SimpleLazy<T> implements Lazy<T> {
        protected T value;
        protected final Supplier<T> ctor;

        SimpleLazy(Supplier<T> ctor) {
            this.value = null;
            this.ctor = ctor;
        }

        @Override
        public T get() {
            if (value == null) {
                value = ctor.get();
            }
            return value;
        }
    }

    static class SyncLazy<T> extends SimpleLazy<T> {
        protected ReadWriteLock rwLock = new ReentrantReadWriteLock();

        protected SyncLazy(Supplier<T> ctor) {
            super(ctor);
        }

        @Override
        public T get() {
            // First try to read existing value
            rwLock.readLock().lock();
            T tmp = value;
            rwLock.readLock().unlock();
            // If it's null, try to initialize
            if (tmp == null) {
                rwLock.writeLock().lock();
                try {
                    // Someone else may have initialized it while we weren't looking
                    if (value == null) {
                        value = ctor.get();
                    }
                    tmp = value;
                } finally {
                    // ALWAYS release write-lock so we don't deadlock
                    rwLock.writeLock().unlock();
                }
            }
            return tmp;
        }
    }
}