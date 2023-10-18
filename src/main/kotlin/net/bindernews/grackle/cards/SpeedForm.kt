package net.bindernews.grackle.cards

import basemod.helpers.BaseModCardTags
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.cardmods.AutoDescription
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.power.SpeedFormPower

class SpeedForm : BaseCard(C, VARS) {

    init {
        isEthereal = true
        initializeDescription()
    }

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop.actionApplyPower(p, p, SpeedFormPower.POWER_ID, magicNumber))
    }

    override fun initializeDescription() {
        rawDescription = C.strings.DESCRIPTION
        super.initializeDescription()
    }

    companion object {
        @JvmField val C = CardConfig("SpeedForm", CardType.POWER, CardRarity.RARE, CardTarget.SELF)
        @JvmField val VARS = CardVariables().apply {
            cost(3)
            magic(6)
            addModifier(AutoDescription())
            onUpgrade {
                it.isEthereal = false
                it.initializeDescription()
            }
            tags(BaseModCardTags.FORM)
        }
    }
}