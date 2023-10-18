package net.bindernews.grackle.cardmods

import basemod.abstracts.AbstractCardModifier
import basemod.abstracts.AbstractCardModifier.SaveIgnore
import basemod.cardmods.EtherealMod
import basemod.cardmods.ExhaustMod
import basemod.cardmods.InnateMod
import basemod.cardmods.RetainMod
import com.megacrit.cardcrawl.cards.AbstractCard

/**
 * Automatically updates the card's description based on the values
 * of [AbstractCard.exhaust], [AbstractCard.retain], and [AbstractCard.isInnate].
 *
 * @author bindernews
 */
@SaveIgnore
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
        if (card.isEthereal) {
            tmp = etherealMod.modifyDescription(tmp, card)
        }
        return tmp
    }

    override fun isInherent(card: AbstractCard?): Boolean = true

    override fun makeCopy(): AbstractCardModifier = AutoDescription()

    companion object {
        private val exhaustMod = ExhaustMod()
        private val retainMod = RetainMod()
        private val innateMod = InnateMod()
        private val etherealMod = EtherealMod()
    }
}