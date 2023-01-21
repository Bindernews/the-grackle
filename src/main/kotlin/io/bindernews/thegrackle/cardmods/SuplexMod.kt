package io.bindernews.thegrackle.cardmods

import basemod.abstracts.AbstractCardModifier
import com.megacrit.cardcrawl.cards.AbstractCard
import io.bindernews.thegrackle.variables.ExtraHitsVariable

class SuplexMod : AbstractCardModifier() {
    init {
        // Decrease priority
        priority = 1
    }

    override fun onApplyPowers(card: AbstractCard) {
        card.damage += card.baseMagicNumber * ExtraHitsVariable.inst.value(card)
    }

    override fun makeCopy(): AbstractCardModifier {
        return SuplexMod()
    }
}