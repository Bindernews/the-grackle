package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.cardmods.AutoDescription
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder
import net.bindernews.grackle.power.TempSpeedPower

class PeckingOrder : BaseCard(C, VARS) {
    override val descriptionSource get() = descriptionBuilder

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(ApplyPowerAction(p, p, TempSpeedPower(p, magicNumber), magicNumber))
    }

    companion object {
        @JvmField val C = CardConfig("PeckingOrder", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        val VARS = CardVariables().apply {
            cost(1, -1)
            magic(2)
            addModifier(AutoDescription())
            onInit {
                it.exhaust = true
                it.initializeDescription()
            }
            onUpgrade {
                it.retain = true
                it.selfRetain = true
                it.initializeDescription()
            }
        }

        val descriptionBuilder = DescriptionBuilder.create {
            when (lang) {
                else -> format("{Gain} !M! temporary {Speed}.")
            }
        }
    }
}