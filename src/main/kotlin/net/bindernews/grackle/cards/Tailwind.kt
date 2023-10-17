package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.DrawPower
import net.bindernews.grackle.cardmods.AutoDescription
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.ModInterop

class Tailwind : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val power = ModInterop.iop().createPower(DrawPower.POWER_ID, p, magicNumber)
        addToBot(ApplyPowerAction(p, p, power, magicNumber))
    }

    companion object {
        @JvmField val C = CardConfig("Tailwind", CardType.POWER, CardRarity.RARE, CardTarget.SELF)
        val VARS = CardVariables().apply {
            cost(1)
            magic(1, -1)
            onUpgrade {
                it.isInnate = true
                it.initializeDescription()
            }
            addModifier(AutoDescription())
        }
    }
}