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
        @JvmStatic val C = CardConfig("MidairRefuel", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        @JvmStatic val VARS = CardVariables().apply {
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