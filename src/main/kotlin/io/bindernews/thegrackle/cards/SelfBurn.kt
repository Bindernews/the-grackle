package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.actions.common.DrawCardAction
import com.megacrit.cardcrawl.cards.status.Burn
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.helper.ModInterop.Companion.iop

@Suppress("MemberVisibilityCanBePrivate")
class SelfBurn : BaseCard(C, VARS) {
    /**
     * If true, draws cards immediately, otherwise applies a power to draw extra cards next turn.
     * This is intended to be used by boss characters.
     */
    var drawNow = true
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        if (drawNow) {
            addToBot(DrawCardAction(p, magicNumber))
        } else {
            addToBot(iop().actionApplyPower(p, p, "Draw Card", magicNumber))
        }
        addToBot(iop().actionMakeTempCardInDiscard(p, Burn(), BURN_COUNT))
    }

    companion object {
        @JvmStatic val C = CardConfig("SelfBurn", CardType.SKILL, CardRarity.COMMON, CardTarget.NONE)
        val VARS = CardVariables().apply {
            cost(0)
            magic(1, 2)
            onInit { it.cardsToPreview = Burn() }
        }
        const val BURN_COUNT = 1
    }
}