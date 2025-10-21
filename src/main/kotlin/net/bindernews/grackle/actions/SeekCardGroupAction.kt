package net.bindernews.grackle.actions

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.defect.SeekAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon

class SeekCardGroupAction(
    private val source: () -> CardGroup,
    public var amount: Int,
) : AbstractGameAction() {

    private var cardGroup: CardGroup? = null
    private val p: AbstractPlayer = AbstractDungeon.player

    init {
        setValues(p, p, amount)
        actionType = ActionType.CARD_MANIPULATION
        duration = Settings.ACTION_DUR_MED
    }

    override fun update() {
        if (cardGroup == null) {
            cardGroup = source()
        }
        val cards = cardGroup!!

        if (cards.size() == 0) {
            isDone = true
        } else if (cards.size() <= this.amount) {
            for (i in 0 until cards.size()) {
                val card: AbstractCard = cards.getNCardFromTop(i)
                if (p.hand.size() >= p.gameHandSize) {
                    p.drawPile.moveToDiscardPile(card)
                    p.createHandIsFullDialog()
                } else {
                    card.unhover()
                    card.lighten(true)
                    card.setAngle(0.0f)
                    card.drawScale = 0.12f
                    card.targetDrawScale = 0.75f
                    card.current_x = CardGroup.DRAW_PILE_X
                    card.current_y = CardGroup.DRAW_PILE_Y
                    p.drawPile.removeCard(card)
                    p.hand.addToTop(card)
                    p.hand.refreshHandLayout()
                    p.hand.applyPowers()
                }
            }
            this.isDone = true
        } else {
            if (amount == 1) {
                AbstractDungeon.gridSelectScreen.open(cards, amount, SeekAction.TEXT[0], false)
            } else {
                AbstractDungeon.gridSelectScreen.open(cards, amount, SeekAction.TEXT[1], false)
            }
            tickDuration()
        }
    }

    companion object {
        fun getTopNCards(group: CardGroup, n: Int): CardGroup {
            val tmp = CardGroup(group.type)
            val cap = n.coerceAtMost(group.size())
            for (i in 0 until cap) {
                tmp.group.add(group.getNCardFromTop(i))
            }
            return tmp
        }
    }
}