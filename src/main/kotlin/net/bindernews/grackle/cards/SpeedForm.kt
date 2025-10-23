package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.cardmods.AutoDescription
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.power.SpeedFormPower

class SpeedForm : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop.actionApplyPower(p, p, SpeedFormPower.POWER_ID, 1));
    }

    companion object {
        @JvmField val C: CardConfig = CardConfig("SpeedForm", CardType.POWER, CardRarity.RARE, CardTarget.SELF)
        @JvmField val VARS = CardVariables().apply {
            cost(3, -1)
            onInit { c -> c.isEthereal = true }
            onUpgrade { c -> c.isEthereal = false }
            addModifier(AutoDescription())
        }
    }
}