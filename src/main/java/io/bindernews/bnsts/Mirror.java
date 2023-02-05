package io.bindernews.bnsts;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Arrays;

public final class Mirror {
    private Mirror() {}

    public static MethodHandle findHandle(Class<?> clz, String name) {
        try {
            return MethodHandles.lookup().unreflect(findMethod(clz, name));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Method findMethod(Class<?> clz, String name) {
        Method[] ar = Arrays.stream(clz.getDeclaredMethods())
                .filter(m -> m.getName().equals(name))
                .toArray(Method[]::new);
        if (ar.length == 1) {
            ar[0].setAccessible(true);
            return ar[0];
        } else if (ar.length == 0) {
            throw new RuntimeException(new NoSuchMethodException(clz.getName() + ": " + name));
        } else {
            throw new RuntimeException("too many methods named '" + name + "' in " + clz.getName());
        }
    }
}
