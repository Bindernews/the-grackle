package io.bindernews.thegrackle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.metrics.Metrics;
import io.bindernews.bnsts.eventbus.*;
import io.bindernews.thegrackle.interfaces.SvcChangeCardEvent;

/**
 * A collection of {@link EventEmit}s, serving as a global event registry.
 */
public class Events {
    private Events() {}

    private static final EventEmit<SpriteBatch> onPopupRender = new EventEmit<>();
    private static final EventEmit<SvcChangeCardEvent> svcCardChange = new EventEmit<>();
    private static final EventEmit<Metrics> metricsRun = new EventEmit<>();

    /** Listeners for popup rendering. This uses {@link EventEmit} for performance. */
    public static IEventEmit<SpriteBatch> popupRender() { return onPopupRender; }

    /** Event for when {@link com.megacrit.cardcrawl.screens.SingleCardViewPopup} changes cards. */
    public static IEventEmit<SvcChangeCardEvent> svcCardChange() { return svcCardChange; }

    /** Event emitter, called at the end of {@link Metrics#run}. */
    public static IEventEmit<Metrics> metricsRun() { return metricsRun; }
}
