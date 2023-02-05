package io.bindernews.thegrackle

import com.megacrit.cardcrawl.metrics.Metrics
import com.megacrit.cardcrawl.screens.SingleCardViewPopup
import io.bindernews.bnsts.eventbus.EventEmit
import io.bindernews.bnsts.eventbus.HandlerList
import io.bindernews.bnsts.eventbus.IEventEmit
import io.bindernews.bnsts.eventbus.IHandlerList
import io.bindernews.thegrackle.api.IPopup
import io.bindernews.thegrackle.api.SvcChangeCardEvent

/**
 * A collection of [EventEmit]s, serving as a global event registry.
 */
object Events {

    /** Event for when [SingleCardViewPopup] changes cards.  */
    @JvmStatic val svcCardChange: IEventEmit<SvcChangeCardEvent> = EventEmit()
    /** Event emitter, called at the end of [Metrics.run].  */
    @JvmStatic val metricsRun: IEventEmit<Metrics> = EventEmit()
    /** List of UI popups */
    @JvmStatic val popups: IHandlerList<IPopup> = HandlerList()
}