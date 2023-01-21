package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.VulnerablePower
import com.megacrit.cardcrawl.powers.WeakPower
import io.bindernews.bnsts.CardVariables

class Cackle : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature) {
        addToBot(ApplyPowerAction(m, p, WeakPower(m, magicNumber, false), magicNumber))
        addToBot(ApplyPowerAction(m, p, VulnerablePower(m, magicNumber, false), magicNumber))
    }

    companion object {
        @JvmField val C = CardConfig("Cackle", CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY)
        val VARS = CardVariables.config { c ->
            c.cost(0, -1)
            c.magic(1, 2)
        }
    }
}