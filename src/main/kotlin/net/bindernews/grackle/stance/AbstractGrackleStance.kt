package net.bindernews.grackle.stance

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.stances.AbstractStance
import net.bindernews.grackle.relics.BorrowedMoment
import net.bindernews.sts.slots.SlotHelper

abstract class AbstractGrackleStance : AbstractStance() {

    abstract val slots: SlotHelper

    /**
     * Timer
     */
    var flashTimer: Float = 0.0f

    /**
     * Called to trigger the technique.
     */
    abstract fun technique()

    override fun onEnterStance() {
        if (AbstractDungeon.player.hasRelic(BorrowedMoment.ID)) {
            slots.count = 4
        } else {
            slots.count = 3
        }
    }

    override fun onPlayCard(card: AbstractCard) {
        if (card.type == AbstractCard.CardType.SKILL && slots.spendSlot()) {
            technique()
        }
    }

    override fun update() {
        slots.update()
        flashTimer -= Gdx.graphics.deltaTime
        if (this.flashTimer < 1.0f) {
            this.flashTimer = 1.0f
        }
    }

    override fun render(sb: SpriteBatch) {
        super.render(sb)
        slots.render(sb)
    }




}