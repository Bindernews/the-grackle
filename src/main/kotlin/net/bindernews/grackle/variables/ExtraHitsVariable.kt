package net.bindernews.grackle.variables

import basemod.helpers.CardModifierManager
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum
import com.evacipated.cardcrawl.modthespire.lib.SpireField
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.eventbus.EventEmit
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.api.HitCountEvent
import net.bindernews.grackle.api.IMultiHitManager
import net.bindernews.grackle.cardmods.ExtraHitsMod
import net.bindernews.grackle.helper.ModInterop
import java.util.function.Consumer

class ExtraHitsVariable : AbstractSimpleVariable(Fields.extraHits, VariableInst()), IMultiHitManager {
    override fun key(): String = KEY

    /**
     * Returns the number of extra hits for a creature. This MAY be the same
     * value as calculated by [.applyPowers], but not always.
     * @param source Creature emitting the event
     * @param initial Initial hit amount
     * @return number of extra hits
     */
    override fun getExtraHits(source: AbstractCreature?, initial: Int): Int {
        if (source == null) {
            return 0
        }
        val event = HitCountEvent(source, null, emptySet(), 0, 0)
        onApplyPowers.emit(event)
        // Limit to 0
        return event.count.coerceAtLeast(-initial)
    }

    override fun getExtraHitsCard(card: AbstractCard, initial: Int): Int {
        // Limit final hit count to 0
        return inst.value(card).coerceAtLeast(-initial)
    }

    override fun makeMultiHit(card: AbstractCard) {
        CardModifierManager.addModifier(card, ExtraHitsMod())
    }

    /**
     * Callback for [AbstractCard.applyPowers]
     */
    fun applyPowers(card: AbstractCard) {
        if (baseValue(card) != -1) {
            val source = ModInterop.iop().getCardOwner(card)
            val event = HitCountEvent(source!!, card, emptySet(), 0, 0)
            onApplyPowers.emit(event)
            // Limit final hit count to 0
            val extra = event.count.coerceAtLeast(0)
            setValue(card, baseValue(card) + extra)
        }
    }

    fun resetAttributes(card: AbstractCard) {
        setValue(card, baseValue(card))
    }

    @SpirePatch(clz = AbstractCard::class, method = SpirePatch.CLASS)
    object Fields {
        @JvmField var extraHits = SpireField<VariableInst?> { null }
    }

    companion object {
        val KEY = GrackleMod.makeId("hits")

        /**
         * Indicates that the card supports multi-hit.
         */
        @SpireEnum lateinit var GK_MULTI_HIT: AbstractCard.CardTags
        @JvmField val inst = ExtraHitsVariable()
        @JvmField val onApplyPowers = EventEmit<HitCountEvent>()

        /**
         * Utility function to create a listener that will add io.bindernews.thegrackle.helper.hits based on
         * how much of a certain power the source creature has.
         * @param powerId Power ID to use
         * @return Event handler
         */
        @JvmStatic
        fun addPowerAmount(powerId: String): Consumer<HitCountEvent> {
            return Consumer { ev -> ev.addCount(ev.source.getPower(powerId)?.amount ?: 0) }
        }
    }
}