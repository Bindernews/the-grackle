package net.bindernews.grackle.actions

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.core.CardCrawlGame

class SoundAction(sound: String) : AbstractGameAction() {
    private val sfx: String

    init {
        actionType = ActionType.CARD_MANIPULATION
        duration = DEFAULT_DURATION
        sfx = sound
    }

    override fun update() {
        CardCrawlGame.sound.play(sfx)
        isDone = true
    }
}