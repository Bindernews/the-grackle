package io.bindernews.thegrackle.cards

import basemod.cardmods.InnateMod
import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.DrawPower
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.helper.ModInterop

class Tailwind : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val power = ModInterop.iop().createPower(DrawPower.POWER_ID, p, magicNumber)
        addToBot(ApplyPowerAction(p, p, power, magicNumber))
    }
    companion object {
        @JvmStatic val C = CardConfig("Tailwind", CardType.POWER, CardRarity.RARE, CardTarget.SELF)
        @JvmStatic val VARS = CardVariables().apply {
            cost(1)
            magic(1, -1)
            onUpgrade { CardModifierManager.addModifier(it, InnateMod()) }
        }
    }
}