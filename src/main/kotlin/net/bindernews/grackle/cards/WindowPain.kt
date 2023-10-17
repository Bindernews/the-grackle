package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.power.TempThornsPower

class WindowPain : BaseCard(C, VARS) {
    override val descriptionSource get() = DESCRIPTION_BUILDER

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(GainBlockAction(p, p, block))
        addToBot(iop().actionApplyPower(p, p, TempThornsPower.POWER_ID, magicNumber))
    }

    companion object {
        @JvmField val C = CardConfig("WindowPain", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        @JvmField val VARS = CardVariables().apply {
            cost(1)
            block(6, 10)
            magic(2, 3)
        }

        @JvmField val DESCRIPTION_BUILDER = DescriptionBuilder.create {
            format("{Gain} !B! {Block}. NL {Gain} !M! {temporary} {Thorns}.")
        }
    }
}