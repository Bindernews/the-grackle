package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder
import net.bindernews.grackle.power.SpeedPower

class Takeoff : BaseCard(C, VARS) {
    override val descriptionSource get() = descriptionBuilder

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(GainBlockAction(p, p, block))
        addToBot(iop.actionApplyPower(p, p, SpeedPower.POWER_ID, magicNumber))
    }

    companion object {
        @JvmField val C = CardConfig("Takeoff", CardType.SKILL, CardRarity.BASIC, CardTarget.SELF)
        val VARS = CardVariables.config { c ->
            c.cost(1, -1)
            c.block(6, 10)
            c.magic(3, 6)
            c.tags(CardTags.STARTER_DEFEND)
        }

        val descriptionBuilder = DescriptionBuilder.create {
            when (lang) {
                else -> format("{Gain} !B! {Block}. NL {Gain} !M! {Speed}.")
            }
        }

    }
}