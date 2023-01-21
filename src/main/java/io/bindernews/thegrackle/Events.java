package io.bindernews.thegrackle;

import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import io.bindernews.bnsts.eventbus.*;
import io.bindernews.thegrackle.api.IPopup;
import io.bindernews.thegrackle.api.SvcChangeCardEvent;

/**
 * A collection of {@link EventEmit}s, serving as a global event registry.
 */
public class Events {

    private Events() {}

    private static final EventEmit<SvcChangeCardEvent> svcCardChange = new EventEmit<>();
    private static final EventEmit<Metrics> metricsRun = new EventEmit<>();
    private static final HandlerList<IPopup> popups = new HandlerList<>();

    /** Event for when {@link SingleCardViewPopup} changes cards. */
    public static IEventEmit<SvcChangeCardEvent> svcCardChange() { return svcCardChange; }

    /** Event emitter, called at the end of {@link Metrics#run}. */
    public static IEventEmit<Metrics> metricsRun() { return metricsRun; }
    
    public static IHandlerList<IPopup> popups() { return popups; }
}
