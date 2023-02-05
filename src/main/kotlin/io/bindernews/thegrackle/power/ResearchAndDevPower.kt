package io.bindernews.thegrackle.power

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import io.bindernews.thegrackle.GrackleMod
import io.bindernews.thegrackle.vfx.UpgradeRandomCardEffect

class ResearchAndDevPower(owner: AbstractCreature, amount: Int) : BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, amount)
        loadRegion("ai")
        updateDescription()
    }

    override fun wasHPLost(info: DamageInfo, damageAmount: Int) {
        if (damageAmount > 0) {
            addToBot(RemoveSpecificPowerAction(owner, owner, this))
        }
    }

    override fun onVictory() {
        for (i in 0 until amount) {
            AbstractDungeon.effectsQueue.add(UpgradeRandomCardEffect(owner))
        }
    }

    override fun updateDescription() {
        description = formatDesc(0)
    }

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(ResearchAndDevPower::class.java)
    }
}