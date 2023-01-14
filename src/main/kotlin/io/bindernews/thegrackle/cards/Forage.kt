package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.helpers.GameDictionary
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.helper.ModInterop.iop

class Forage : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop().actionGainEnergy(p, magicNumber))
    }

    override fun initializeDescription() {
        if (exhaust) {
            rawDescription += " NL ${GameDictionary.EXHAUST.NAMES[0]}."
        }
        super.initializeDescription()
    }

    companion object {
        @JvmStatic val C = CardConfig("Forage", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        @JvmStatic val VARS = CardVariables().apply {
            cost(0)
            magic(1, -1)
            exhaust(true, false)
            onUpgrade { it.initializeDescription() }
        }
    }
}