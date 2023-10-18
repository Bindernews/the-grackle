package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.EnergizedBluePower
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder

class PeckingOrder : BaseCard(C, VARS) {
    override val descriptionSource get() = descriptionBuilder

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop.actionApplyPower(p, p, EnergizedBluePower.POWER_ID, ENERGY_GAIN))
    }

    companion object {
        @JvmField val C = CardConfig("PeckingOrder", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        const val ENERGY_GAIN = 2
        val VARS = CardVariables().apply {
            cost(1, 0)
        }

        val descriptionBuilder = DescriptionBuilder.create {
            val eng = energyStr(ENERGY_GAIN)
            when (lang) {
                else -> format("{Gain}$eng next turn.")
            }
        }
    }
}