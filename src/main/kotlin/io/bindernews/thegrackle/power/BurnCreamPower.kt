package io.bindernews.thegrackle.power

import basemod.cardmods.ExhaustMod
import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.thegrackle.GrackleMod
import io.bindernews.thegrackle.helper.BurnHelper.isBurn

class BurnCreamPower(owner: AbstractCreature, @Suppress("UNUSED_PARAMETER") amount: Int) : BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, -1)
        isTurnBased = false
        type = PowerType.BUFF
        loadRegion("evolve")
        updateDescription()
    }

    override fun onCardDraw(card: AbstractCard) {
        if (isBurn(card)) {
            CardModifierManager.addModifier(card, ExhaustMod())
        }
    }

    override fun updateDescription() {
        description = formatDesc(0)
    }

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(BurnCreamPower::class.java)
    }
}