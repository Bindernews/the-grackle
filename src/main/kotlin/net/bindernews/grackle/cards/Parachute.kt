package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder
import net.bindernews.grackle.helper.speedBoost
import net.bindernews.grackle.power.SpeedPower

class Parachute : BaseCard(C, VARS) {
    override val descriptionSource get() = descriptionBuilder

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val blockAmt = if(SpeedPower.tryBoost(p, this)) block * 2 else block
        addToBot(GainBlockAction(p, blockAmt))
    }

    override fun isBonusActive(): Boolean = isBonusActive(owner!!)

    companion object {
        @JvmField val C = CardConfig("Parachute", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        val VARS = CardVariables.config { c ->
            c.cost(1, -1)
            c.block(8, 11)
            c.speedBoost(4, -1)
        }

        val descriptionBuilder = DescriptionBuilder.create {
            format("{Gain} !B! {Block}. NL {speed_boost} NL double {Block}.")
        }
    }
}