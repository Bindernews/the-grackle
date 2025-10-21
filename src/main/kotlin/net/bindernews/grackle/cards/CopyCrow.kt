package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.unique.NightmareAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables

class CopyCrow : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(NightmareAction(p, p, 1))
    }

    companion object {
        @JvmField val C = CardConfig("CopyCrow", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF)
        @JvmField val VARS = CardVariables().apply {
            cost(1, 0)
        }
    }
}