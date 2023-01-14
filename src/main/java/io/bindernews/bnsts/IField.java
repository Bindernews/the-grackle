package io.bindernews.bnsts;

import lombok.SneakyThrows;
import lombok.val;

import java.lang.invoke.MethodHandles;
import java.util.function.Function;

public interface IField<T, F> extends Function<T, F> {
    default F get(T obj) {
        return apply(obj);
    }

    void set(T obj, F value);


    public static <T, F> IField<T, F> unreflect(Class<T> clz, String name) {
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
