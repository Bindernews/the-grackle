package io.bindernews.bnsts.eventbus;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class EventBusTest {

    @Test
    void registerGood() {
        assertDoesNotThrow(() -> {
            val bus = new EventBus();
            bus.register(Handler1.class, "accept");
        });
    }

    @Test
    void registerBad() {
        assertThrows(EventBusException.class, () -> {
            val bus = new EventBus();
            bus.register(Handler1.class, "accepting");
        });
    }

    @Test
    void reifyOnGood() {
        assertDoesNotThrow(() -> {
            val bus = makeTestBus();
            bus.<Handler1>on((a, b) -> {
                System.out.println(a + b);
            });
        });
    }

    @Test
    void reifyOnLocalMethod() {
        assertDoesNotThrow(() -> {
            val bus = makeTestBus();
            bus.<Handler2>on(this::reifyTest2);
        });
    }

    void reifyTest2(String s) {
        System.out.println(s);
    }

    @Test
    void priorityOrderCheck() {
        // TODO implement
    }

    @Test
    void off() {
    }

    @Test
    void emit1() {
        val bus = makeTestBus();
        val out = new int[2];
        bus.<Handler1>on((a, b) -> {
            out[0] = a + b;
        });
        bus.<Handler1>on((a, b) -> {
            out[1] = a * b;
        });
        bus.emit(Handler1.class, 2, 3);
        assertArrayEquals(new int[]{5, 6}, out);
    }


    private EventBus makeTestBus() {
        val bus = new EventBus();
        bus.register(Handler1.class, "accept");
        bus.register(Handler2.class, "accept");
        return bus;
    }

    @FunctionalInterface
    public interface Handler1 {
        void accept(int a, int b);
    }

    public interface Handler2 extends Consumer<String> {}

}