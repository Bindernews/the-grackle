package net.bindernews.grackle.cards

import basemod.cardmods.ExhaustMod
import basemod.helpers.CardModifierManager.removeModifiersById
import com.megacrit.cardcrawl.core.AbstractCreature
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
            magic(1, -1)
            addModifier(ExhaustMod())
            onUpgrade { removeModifiersById(it, ExhaustMod.ID, true) }
        }
    }
}