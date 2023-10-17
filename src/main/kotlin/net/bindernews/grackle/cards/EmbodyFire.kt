package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.power.EmbodyFirePower

class EmbodyFire : BaseCard(C, VARS) {
    override val descriptionSource get() = descriptionBuilder

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop().actionApplyPower(p, p, EmbodyFirePower.POWER_ID, magicNumber))
    }

    companion object {
        @JvmStatic val C = CardConfig("EmbodyFire", CardType.POWER, CardRarity.RARE, CardTarget.SELF)
        @JvmStatic val VARS = CardVariables().apply {
            cost(2)
            magic(2)
            onUpgrade {
                it.isInnate = true
                it.initializeDescription()
            }
        }

        val descriptionBuilder = DescriptionBuilder.create { upg ->
            val innate = if (upg) format("{Innate}. NL ") else ""
            when (lang) {
                else -> innate + format("{+start_of_turn} apply !M! grackle:Burning to ALL enemies.")
            }
        }
    }
}