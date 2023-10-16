package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.DrawCardAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.stances.NeutralStance
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder

class Dive : BaseCard(C, VARS) {

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(DrawCardAction(p, magicNumber))
        addToBot(iop.changeStance(p, NeutralStance.STANCE_ID))
    }

    override fun initializeDescription() {
        rawDescription = RAW_DESCRIPTION
        super.initializeDescription()
    }

    companion object {
        @JvmField val C = CardConfig("Dive", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF)
        @JvmField val VARS = CardVariables().apply {
            cost(1)
            magic(2, 3)
        }

        val RAW_DESCRIPTION = DescriptionBuilder.create()
            .tr("draw").add("!M!").tr("cards").period(true)
            .exitStance().period(false)
            .build()
    }
}