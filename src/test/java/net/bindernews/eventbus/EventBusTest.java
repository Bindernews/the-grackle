package net.bindernews.eventbus;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class EventBusTest {

    @Test
    void registerGood() {
        assertDoesNotThrow(() -> {
            val bus = new EventBus();
            bus.register(Handler1.class);
        });
    }

    @Test
    void registerBad() {
//        assertThrows(EventBusException.class, () -> {
//            val bus = new EventBus();
//            bus.register(Handler1.class);
//        });
    }

    @Test
    void registerEvent() {
        assertDoesNotThrow(() -> {
            val bus = new EventBus();
            bus.registerConsumer(Handler2.class);
        });
    }

    @Test
    void reifyOnGood() {
        assertDoesNotThrow(() -> {
            val bus = makeTestBus();
            bus.<Handler1>on((a, b) -> System.out.println(a + b));
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
        val bus = makeTestBus();
        int[] out = new int[]{5};
        Handler2 h = s -> out[0] += 2;
        bus.on(h);
        bus.emitEvent("a");
        bus.off(h);
        bus.emitEvent("a");
        assertEquals(7, out[0]);
    }

    @Test
    void once() {
        val bus = makeTestBus();
        int[] out = new int[]{5};
        bus.<Handler2>once(s -> out[0] += 2);
        bus.emitEvent("a");
        bus.emitEvent("a");
        assertEquals(7, out[0]);
    }

    @Test
    void emitEvent() {
        val bus = makeTestBus();
        String[] out = new String[1];
        bus.<Handler2>on(s -> out[0] = s);
        bus.emitEvent("test");
        assertEquals(out[0], "test");
    }

    @Test
    void emit1() {
        val bus = makeTestBus();
        val out = new int[2];
        bus.<Handler1>on((a, b) -> out[0] = a + b);
        bus.<Handler1>on((a, b) -> out[1] = a * b);
        bus.get(Handler1.class).forEach(h -> h.accept(2, 3));
//        bus.<Handler1>callEach(h -> h.accept(2, 3));
        assertArrayEquals(new int[]{5, 6}, out);
    }

    @Test
    void getEventClass() {
        val handle = EventBus.findHandle(HandlerGetClass.class, "accept");
        val clz = EventBus.getEventClass(handle);
        assertEquals(String.class, clz);
    }

    @Test
    void testHas() {
        val bus = makeTestBus();
        assertTrue(bus.has(Handler2.class));
    }

    @Test
    void testNotHas() {
        val bus = makeTestBus();
        assertFalse(bus.has(HandlerInvalid.class));
    }

    private EventBus makeTestBus() {
        val bus = new EventBus();
        bus.register(Handler1.class);
        bus.registerConsumer(Handler2.class);
        return bus;
    }

    @FunctionalInterface
    public interface Handler1 {
        void accept(int a, int b);
    }

    public interface Handler2 extends Consumer<String> {}

    public interface HandlerInvalid extends Consumer<StringBuilder> {}

    public interface HandlerGetClass {
        void accept(String s);
    }
}