package io.bindernews.bnsts;

import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;
import java.util.function.Function;

public interface IField<T, F> extends Function<T, F> {
    default F get(T obj) {
        return apply(obj);
    }

    void set(T obj, F value);


    /**
     * Create an {@link IField} instance which can get and set the named field of {@code clz}.
     * <br/>
     * Note that if the field is read-only, this will not crash unless you try to set it, making
     * it suitable for use with {@code final} fields.
     *
     * @param clz Class with the field to access
     * @param name Name of the field
     * @return a new {@code IField} instance
     * @param <T> Class type
     * @param <F> Field type
     */
    static <T, F> @NotNull IField<T, F> unreflect(Class<T> clz, String name) {
        try {
            val lookup = MethodHandles.lookup();
            val m = clz.getDeclaredField(name);
            m.setAccessible(true);
            val hget = lookup.unreflectGetter(m);
            val hset = lookup.unreflectSetter(m);
            return new IField<T, F>() {
                @Override @SneakyThrows @SuppressWarnings("unchecked")
                public F apply(T o) { return (F) hget.invoke(o); }
                @Override @SneakyThrows
                public void set(T o, F value) { hset.invoke(o, value); }
            };
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
