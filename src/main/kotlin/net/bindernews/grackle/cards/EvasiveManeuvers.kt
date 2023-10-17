package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.actions.utility.ScryAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder
import net.bindernews.grackle.helper.magic2
import net.bindernews.grackle.power.SpeedPower

class EvasiveManeuvers : BaseCard(C, VARS) {
    override val descriptionSource get() = descriptionBuilder

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(GainBlockAction(p, p, block))
        addToBot(ScryAction(magicNumber))
        addToBot(iop.actionApplyPower(p, p, SpeedPower.POWER_ID, magic2))
    }

    companion object {
        @JvmField val C = CardConfig("EvasiveManeuvers", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        val VARS = CardVariables.config { c ->
            c.cost(1, -1)
            c.block(7, 10)
            c.magic(3, 5)
            c.magic2(2, 4)
        }

        val descriptionBuilder = DescriptionBuilder.create {
            format("{Gain} !B! {Block}. NL {Scry} !M!. NL {Gain} {magic2} {Speed}.")
        }
    }
}