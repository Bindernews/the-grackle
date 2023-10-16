package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.watcher.VigorPower
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder
import net.bindernews.grackle.stance.StanceEagle

class Perch : BaseCard(C, VARS) {

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop.actionGainEnergy(p, cost))
        addToBot(iop.actionApplyPower(p, p, VigorPower.POWER_ID, magicNumber))
        addToBot(iop.changeStance(p, StanceEagle.STANCE_ID))
    }

    override fun initializeDescription() {
        rawDescription = RAW_DESCRIPTION
        super.initializeDescription()
    }

    companion object {
        @JvmField val C = CardConfig("Perch", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF)
        @JvmField val VARS = CardVariables().apply {
            cost(1)
            magic(2, 3)
        }

        val RAW_DESCRIPTION = DescriptionBuilder.create()
            .tr("gain").add("1 [E]").period(true)
            .tr("gain").add("!M! Vigor").period(true)
            .enterStance("Eagle").period(false)
            .build()
    }
}