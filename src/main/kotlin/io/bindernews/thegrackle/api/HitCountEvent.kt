package io.bindernews.thegrackle.api

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature

data class HitCountEvent(
    /** Creature that originated the event.  */
    val source: AbstractCreature,
    /** The card that created this event.  */
    val card: AbstractCard?,
    /** Tags indicating damage type, source mod, etc.  */
    val tags: Set<String>,
    /** Initial hit count */
    val baseCount: Int = 0,
    /** Mutable hit count */
    var count: Int = 0,
) {
    fun addCount(deltaCount: Int) {
        count += deltaCount
    }
}
