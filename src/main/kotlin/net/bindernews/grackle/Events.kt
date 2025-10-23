package net.bindernews.grackle

import com.megacrit.cardcrawl.metrics.Metrics
import net.bindernews.eventbus.EventEmit
import net.bindernews.eventbus.IEventEmit

/**
 * A collection of [EventEmit]s, serving as a global event registry.
 */
object Events {
    /** Event emitter, called at the end of [Metrics.run].  */
    @JvmStatic val metricsRun: IEventEmit<Metrics> = EventEmit()
    /** Event for adding additional data to the metrics. */
    @JvmStatic val metricsGather: IEventEmit<Metrics> = EventEmit()
}