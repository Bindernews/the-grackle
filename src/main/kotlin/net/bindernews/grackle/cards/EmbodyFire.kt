package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.helper.StringModifier
import net.bindernews.grackle.power.EmbodyFirePower

class EmbodyFire : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop().actionApplyPower(p, p, EmbodyFirePower.POWER_ID, magicNumber))
    }

    override fun initializeDescription() {
        rawDescription = if (upgraded) descriptions[1] else descriptions[0]
        super.initializeDescription()
    }

    companion object {
        @JvmStatic val C = CardConfig("EmbodyFire", CardType.POWER, CardRarity.RARE, CardTarget.SELF)
        @JvmStatic val VARS = CardVariables().apply {
            cost(2)
            magic(2)
            onUpgrade {
                it.isInnate = true
                it.initializeDescription()
            }
        }

        val descriptions = arrayOf(
            makeDescription(false),
            makeDescription(true),
        )

        private fun makeDescription(upg: Boolean): String {
            return DescriptionBuilder.create()
                .also { if (upg) it.tr("innate").period(true) }
                .atStartOfTurn()
                .applyPower("!M!", "grackle:Burning")
                .toAllEnemies()
                .period(false)
                .build()
        }
    }
}