package net.bindernews.grackle.helper

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.cards.status.Burn
import java.util.stream.Stream

object BurnHelper {
    /**
     * Exhausts all Burn cards in the given group, returns the number of cards exhausted.
     * @param group Card group
     * @return number of cards exhausted
     */
    fun exhaustBurnsInGroup(group: CardGroup): Int {
        val burnCards = ArrayList<AbstractCard>()
        for (card in group.group) {
            if (card.cardID == Burn.ID) {
                burnCards.add(card)
            }
        }
        for (card in burnCards) {
            group.moveToExhaustPile(card)
        }
        return burnCards.size
    }

    fun countBurns(group: CardGroup): Int {
        return getBurns(group).count().toInt()
    }

    fun getBurns(group: CardGroup): Stream<AbstractCard> {
        return group.group.stream().filter(BurnHelper::isBurn)
    }

    @JvmStatic
    fun isBurn(card: AbstractCard): Boolean {
        return card.cardID == Burn.ID
    }
}