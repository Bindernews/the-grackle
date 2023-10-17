package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.watcher.VigorPower
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder
import net.bindernews.grackle.power.SpeedPower

class Perch : BaseCard(C, VARS) {
    override val descriptionSource get() = descriptionBuilder

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop.actionGainEnergy(p, cost))
        addToBot(iop.actionApplyPower(p, p, VigorPower.POWER_ID, magicNumber))
        addToBot(iop.actionApplyPower(p, p, SpeedPower.POWER_ID, magicNumber))
    }

    companion object {
        @JvmField val C = CardConfig("Perch", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF)
        @JvmField val VARS = CardVariables().apply {
            cost(1)
            magic(2, 3)
        }

        val descriptionBuilder = DescriptionBuilder.create {
            when (lang) {
                else -> format("{Gain} [E] , !M! {Vigor}, !M! {Speed}.")
            }
        }
    }
}