package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.actions.SeekCardGroupAction
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder

class ClawShot : BaseCard(C, VARS) {

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        if (p is AbstractPlayer) {
            addToTop(SeekCardGroupAction(
                { SeekCardGroupAction.getTopNCards(p.hand, SCRY_SIZE) },
                magicNumber,
            ))
        }
    }

    override fun initializeDescription() {
        rawDescription = RAW_DESCRIPTION
        super.initializeDescription()
    }

    companion object {
        @JvmStatic val C = CardConfig("ClawShot", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE)
        @JvmStatic val VARS = CardVariables.config { c ->
            c.cost(1, 1)
            c.magic(1, 2)
        }

        const val SCRY_SIZE: Int = 5

        val RAW_DESCRIPTION = DescriptionBuilder.parse(
            "(Choose) !M! (cards) (from) (the top) $SCRY_SIZE(\n)(of your) (draw pile), add them to your hand.")
    }
}