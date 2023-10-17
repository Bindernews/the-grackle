package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.utility.ScryAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.watcher.VigorPower
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder
import net.bindernews.grackle.helper.magic2

class EagleEye : BaseCard(C, VARS) {
    override val descriptionSource get() = DESCRIPTION_BUILDER

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(ScryAction(magicNumber))
        addToBot(iop.actionApplyPower(p, p, VigorPower.POWER_ID, magic2))
    }

    companion object {
        @JvmField val C = CardConfig(this, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF)
        val VARS = CardVariables().apply {
            cost(1)
            magic(3, 5)
            magic2(5, 8)
        }

        val DESCRIPTION_BUILDER = DescriptionBuilder.create {
            format("{Scry} !M!. {Gain} {magic2} {Vigor}.")
        }
    }
}