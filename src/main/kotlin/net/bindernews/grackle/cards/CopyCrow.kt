package net.bindernews.grackle.cards

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.cardmods.AutoDescription
import net.bindernews.grackle.helper.CardVariables

/**
 * Effect: Choose a card, add a copy of it to your hand.
 *
 * NOT boss-compatible.
 */
class CopyCrow : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val msg = C.strings.EXTENDED_DESCRIPTION[0]
        addToBot(SelectCardsInHandAction(
                1, msg, false, false,
                { it.type in allowDuplicateTypes },
                { selected -> dupeCard(selected[0]) }
        ))
    }

    private fun dupeCard(card: AbstractCard) {
        addToBot(MakeTempCardInHandAction(card.makeStatEquivalentCopy()))
    }

    companion object {
        @JvmStatic val C = CardConfig("CopyCrow", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF)
        @JvmStatic val VARS = CardVariables().apply {
            cost(1, 0)
            onInit { it.exhaust = true }
            addModifier(AutoDescription())
        }
        @JvmStatic var allowDuplicateTypes = arrayOf(CardType.SKILL, CardType.POWER, CardType.ATTACK)
    }
}