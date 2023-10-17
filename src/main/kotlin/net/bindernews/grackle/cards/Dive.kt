package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.DrawCardAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder
import net.bindernews.grackle.helper.magic2
import net.bindernews.grackle.power.SpeedPower

class Dive : BaseCard(C, VARS) {
    override val descriptionSource get() = descriptionBuilder

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(DrawCardAction(p, magicNumber))
        addToBot(iop.actionApplyPower(p, p, SpeedPower.POWER_ID, magic2))
    }

    companion object {
        @JvmField val C = CardConfig("Dive", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        @JvmField val VARS = CardVariables().apply {
            cost(0)
            magic(1, 2)
            magic2(2, 4)
        }

        val descriptionBuilder = DescriptionBuilder.create {
            when (lang) {
                else -> format("{Draw} !M! {cards}. NL {Gain} {magic2} {Speed}.")
            }
        }
    }
}