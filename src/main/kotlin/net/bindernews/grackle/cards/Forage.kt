package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.DiscardAction
import com.megacrit.cardcrawl.actions.common.DrawCardAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables

class Forage : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(DrawCardAction(p, magicNumber))
        addToBot(DiscardAction(p, p, 1, false))
    }

    companion object {
        @JvmStatic val C = CardConfig("MidairRefuel", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF)
        val VARS = CardVariables().apply {
            cost(1)
            magic(3, 4)
        }
    }
}