package net.bindernews.grackle.cardmods

import basemod.abstracts.AbstractCardModifier
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.stance.StanceAloft

/**
 * When added to a card, this makes it so that the card can only be played
 * if the user is in [StanceAloft].
 */
class RequireAloftMod : AbstractCardModifier() {
    override fun canPlayCard(card: AbstractCard): Boolean {
        return StanceAloft.checkPlay(card, AbstractDungeon.player, null)
    }

    override fun modifyDescription(rawDescription: String, card: AbstractCard): String {
        val mustBe = GrackleMod.miscUI["must_be"]
        return String.format("%s %s NL %s", mustBe, GrackleMod.CO.KW_ALOFT, rawDescription)
    }

    override fun makeCopy(): AbstractCardModifier {
        return RequireAloftMod()
    }
}