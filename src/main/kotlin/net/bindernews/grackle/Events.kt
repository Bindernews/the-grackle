package net.bindernews.grackle

import com.megacrit.cardcrawl.metrics.Metrics
import com.megacrit.cardcrawl.screens.SingleCardViewPopup
import net.bindernews.eventbus.EventEmit
import net.bindernews.eventbus.IEventEmit
import net.bindernews.grackle.api.IPopup
import net.bindernews.grackle.api.SvcChangeCardEvent
import java.util.*

/**
 * A collection of [EventEmit]s, serving as a global event registry.
 */
object Events {
    /** Event for when [SingleCardViewPopup] changes cards.  */
    @JvmStatic val svcCardChange: IEventEmit<SvcChangeCardEvent> = EventEmit()
    /** Event emitter, called at the end of [Metrics.run].  */
    @JvmStatic val metricsRun: IEventEmit<Metrics> = EventEmit()
    /** List of UI popups */
    @JvmStatic val popups: TreeSet<IPopup> = TreeSet()
    /** Event for adding additional data to the metrics. */
    @JvmStatic val metricsGather: IEventEmit<Metrics> = EventEmit()
}