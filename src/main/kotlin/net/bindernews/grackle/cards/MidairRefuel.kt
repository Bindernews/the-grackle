package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.cardmods.AutoDescription
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.ModInterop.Companion.iop

class MidairRefuel : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop().actionGainEnergy(p, magicNumber))
    }

    companion object {
        @JvmField val C = CardConfig("MidairRefuel", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        val VARS = CardVariables().apply {
            cost(0)
            magic(1)
            onInit { it.exhaust = true; }
            onUpgrade {
                it.exhaust = false
                it.initializeDescription()
            }
            addModifier(AutoDescription())
        }
    }
}