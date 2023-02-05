package io.bindernews.thegrackle.power

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.actions.utility.UseCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.thegrackle.GrackleMod
import io.bindernews.thegrackle.variables.ExtraHitsVariable
import io.bindernews.thegrackle.variables.ExtraHitsVariable.Companion.addPowerAmount

class MultiHitPower(owner: AbstractCreature, amount: Int) : BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, amount)
        loadRegion("vigor")
        updateDescription()
    }

    override fun updateDescription() {
        description = formatDesc(0, amount)
    }

    override fun onUseCard(card: AbstractCard, action: UseCardAction) {
        if (card.type == AbstractCard.CardType.ATTACK && card.hasTag(ExtraHitsVariable.GK_MULTI_HIT)
            && !card.hasTag(GK_MULTI_HIT_PRESERVE)
        ) {
            flash()
            addToBot(RemoveSpecificPowerAction(owner, owner, POWER_ID))
        }
    }

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(MultiHitPower::class.java)

        @SpireEnum
        lateinit var GK_MULTI_HIT_PRESERVE: AbstractCard.CardTags

        init {
            ExtraHitsVariable.onApplyPowers.on(-5, addPowerAmount(POWER_ID))
        }
    }
}