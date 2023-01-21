package io.bindernews.thegrackle.cards

import basemod.cardmods.ExhaustMod
import basemod.helpers.CardModifierManager.removeModifiersById
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.helper.ModInterop.iop

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