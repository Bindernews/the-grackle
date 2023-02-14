package net.bindernews.grackle.cardmods

import basemod.abstracts.AbstractCardModifier
import basemod.cardmods.ExhaustMod
import basemod.cardmods.InnateMod
import basemod.cardmods.RetainMod
import com.megacrit.cardcrawl.cards.AbstractCard

/**
 * Automatically updates the card's description based on the values
 * of [AbstractCard.exhaust], [AbstractCard.retain], and [AbstractCard.isInnate].
 */
open class AutoDescription : AbstractCardModifier() {
    override fun modifyDescription(rawDescription: String, card: AbstractCard): String {
        var tmp = rawDescription
        if (card.exhaust) {
            tmp = exhaustMod.modifyDescription(tmp, card)
        }
        if (card.retain) {
            tmp = retainMod.modifyDescription(tmp, card)
        }
        if (card.isInnate) {
            tmp = innateMod.modifyDescription(tmp, card)
        }
        return tmp
    }

    override fun makeCopy(): AbstractCardModifier = AutoDescription()

    companion object {
        private val exhaustMod = ExhaustMod()
        private val retainMod = RetainMod()
        private val innateMod = InnateMod()
    }
}