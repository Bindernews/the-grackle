package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.DrawCardAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder

class BePrepared : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(DrawCardAction(p, magicNumber))
    }

    companion object {
        @JvmField val C = CardConfig("BePrepared", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        val VARS = CardVariables().apply {
            cost(1, -1)
            magic(2, 3)
        }

        val DESCRIPTION_BUILDER = DescriptionBuilder.create {
            format("{Draw} !M! {cards}.")
        }
    }
}