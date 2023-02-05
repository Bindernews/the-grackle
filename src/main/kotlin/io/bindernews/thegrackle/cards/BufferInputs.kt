package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.thegrackle.helper.CardVariables
import io.bindernews.thegrackle.power.MultiHitPower

class BufferInputs : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(ApplyPowerAction(p, p, MultiHitPower(p, magicNumber), magicNumber))
    }

    companion object {
        @JvmField val C = CardConfig("BufferInputs", CardType.SKILL, CardRarity.RARE, CardTarget.SELF)
        val VARS = CardVariables.config { c ->
            c.cost(1)
            c.magic(2, 4)
        }
    }
}