package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.DrawCardAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.power.SpeedPower

class AerialAdvantage : BaseCard(C, VARS) {
    override val descriptionSource get() = DESCRIPTION_BUILDER

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        // N.B Not applicable for charboss
        addToBot(iop().actionGainEnergy(p, energyGained(upgraded)))
        addToBot(iop().actionApplyPower(p, p, SpeedPower.POWER_ID, magicNumber))
        addToBot(DrawCardAction(p, DRAW_COUNT))
    }

    companion object {
        @JvmField val C = CardConfig("AerialAdvantage", CardType.SKILL, CardRarity.RARE, CardTarget.SELF)
        const val DRAW_COUNT = 3
        val VARS = CardVariables.config { c ->
            c.cost(0)
            c.magic(8, -1)
            c.onInit {
                it.exhaust = true
            }
        }

        fun energyGained(upg: Boolean): Int = if (upg) 2 else 1

        val DESCRIPTION_BUILDER = DescriptionBuilder.create { upg ->
            val eCount = energyStr(energyGained(upg))
            format("{Gain}$eCount . NL {Gain} !M! {Speed}. NL {Draw} $DRAW_COUNT {cards}. NL {Exhaust}.")
        }
    }
}