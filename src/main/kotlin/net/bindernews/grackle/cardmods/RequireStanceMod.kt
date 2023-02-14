package net.bindernews.grackle.cardmods

import basemod.abstracts.AbstractCardModifier
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.stances.NeutralStance
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.helper.ModInterop

/**
 * When added to a card, this makes it so that the card can only be played if in a stance.
 */
class RequireStanceMod : AbstractCardModifier() {
    override fun canPlayCard(card: AbstractCard): Boolean {
        val iop = ModInterop.iop()
        val stanceId = iop.getCardOwner(card)?.let { iop.getStance(it)?.ID } ?: NeutralStance.STANCE_ID
        return if (stanceId == NeutralStance.STANCE_ID) {
            card.cantUseMessage = GrackleMod.miscUI["must_be_stance_err"]
            false
        } else {
            true
        }
    }

    override fun modifyDescription(rawDescription: String, card: AbstractCard): String {
        val mustBe = GrackleMod.miscUI["must_be_stance"]
        return String.format("%s NL %s", mustBe, rawDescription)
    }

    override fun makeCopy(): AbstractCardModifier {
        return RequireStanceMod()
    }
}