package io.bindernews.thegrackle.cardmods

import basemod.abstracts.AbstractCardModifier
import com.megacrit.cardcrawl.cards.AbstractCard
import io.bindernews.thegrackle.variables.ExtraHitsVariable

class ExtraHitsMod : AbstractCardModifier() {
    override fun onInitialApplication(card: AbstractCard) {
        card.tags.add(ExtraHitsVariable.GK_MULTI_HIT)
        // Make sure to initialize the base value to 0 so applyPowers works properly.
        if (ExtraHitsVariable.inst.baseValue(card) == -1) {
            ExtraHitsVariable.inst.setBaseValue(card, 0)
        }
    }

    override fun isInherent(card: AbstractCard): Boolean {
        return true
    }

    override fun onApplyPowers(card: AbstractCard) {
        ExtraHitsVariable.inst.applyPowers(card)
    }

    override fun removeAtEndOfTurn(card: AbstractCard): Boolean {
        ExtraHitsVariable.inst.resetAttributes(card)
        return false
    }

    override fun makeCopy(): AbstractCardModifier {
        return ExtraHitsMod()
    }
}