package io.bindernews.thegrackle.actions

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import io.bindernews.thegrackle.helper.baseExtraHits
import java.util.*

class AddHitsAction(
    private val card: AbstractCard, amount: Int, private val cardGroups: Array<CardGroup>
) : AbstractGameAction() {
    init {
        this.amount = amount
    }

    override fun update() {
        val clz: Class<out AbstractCard> = card.javaClass
        card.baseExtraHits += amount
        card.applyPowers()
        Arrays.stream(cardGroups)
            .flatMap { a: CardGroup -> a.group.stream() }
            .parallel()
            .filter { clz.isInstance(it) }
            .forEach {
                it.baseExtraHits += amount
                it.applyPowers()
            }
        isDone = true
    }

    companion object {
        fun getPlayerCardGroups(): Array<CardGroup> {
            val p = AbstractDungeon.player
            return arrayOf(p.hand, p.discardPile, p.exhaustPile, p.drawPile)
        }
    }
}