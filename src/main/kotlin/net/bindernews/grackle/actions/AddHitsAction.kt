package net.bindernews.grackle.actions

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import net.bindernews.grackle.helper.baseExtraHits
import java.util.*

class AddHitsAction(
    private val card: AbstractCard, amount: Int, private val cardGroups: Array<CardGroup>
) : AbstractGameAction() {
    init {
        this.amount = amount
    }

    override fun update() {
        val clz: Class<out AbstractCard> = card.javaClass
        updateCard(card)
        Arrays.stream(cardGroups)
            .flatMap { it.group.stream() }
            .parallel()
            .filter { clz.isInstance(it) }
            .forEach(this::updateCard)
        isDone = true
    }

    private fun updateCard(card: AbstractCard) {
        card.baseExtraHits += amount
        card.applyPowers()
    }

    companion object {
        fun getPlayerCardGroups(): Array<CardGroup> {
            val p = AbstractDungeon.player
            return arrayOf(p.hand, p.discardPile, p.exhaustPile, p.drawPile)
        }
    }
}