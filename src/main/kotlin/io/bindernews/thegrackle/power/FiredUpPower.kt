package io.bindernews.thegrackle.power

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.status.Burn
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.watcher.VigorPower
import io.bindernews.thegrackle.GrackleMod

class FiredUpPower(owner: AbstractCreature, amount: Int) : BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, amount)
        loadRegion("vigor")
        updateDescription()
    }

    override fun onCardDraw(card: AbstractCard) {
        if (card.cardID == Burn.ID) {
            addToBot(ApplyPowerAction(owner, owner, VigorPower(owner, amount)))
        }
    }

    override fun updateDescription() {
        description = formatDesc(0, amount)
    }

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(FiredUpPower::class.java)
    }
}