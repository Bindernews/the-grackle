package net.bindernews.grackle.cards

import basemod.cardmods.EtherealMod
import com.megacrit.cardcrawl.actions.common.DrawCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.status.Burn
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.helper.addModifier

/**
 * Draws M cards and adds a burn to the discard pile.
 *
 * NOT boss-compatible.
 */
@Suppress("MemberVisibilityCanBePrivate")
class SelfBurn : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(DrawCardAction(p, magicNumber))
        addToBot(iop().actionMakeTempCardInDiscard(p, PREVIEW_BURN.makeStatEquivalentCopy(), BURN_COUNT))
    }

    companion object {
        @JvmStatic val C = CardConfig("SelfBurn", CardType.SKILL, CardRarity.COMMON, CardTarget.NONE)
        val VARS = CardVariables().apply {
            cost(0)
            magic(2, 3)
            onInit { it.cardsToPreview = PREVIEW_BURN }
        }
        const val BURN_COUNT = 1

        val PREVIEW_BURN: AbstractCard = Burn().apply {
            addModifier(EtherealMod())
        }
    }
}