package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.actions.utility.ScryAction
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.bnsts.CardVariables

class EvasiveManeuvers : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(GainBlockAction(p, p, block))
        addToBot(ScryAction(magicNumber))
    }

    companion object {
        @JvmField val C = CardConfig("EvasiveManeuvers", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        val VARS = CardVariables.config { c ->
            c.cost(1, -1)
            c.block(7, 10)
            c.magic(3, 5)
        }
    }
}