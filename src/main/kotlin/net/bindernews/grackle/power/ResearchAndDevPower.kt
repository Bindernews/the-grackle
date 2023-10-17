package net.bindernews.grackle.power

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.vfx.UpgradeRandomCardEffect

/**
 * At the end of combat, upgrades a random card in your deck.
 * Removed if you lose HP. Only applicable to player.
 */
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
            val fx = UpgradeRandomCardEffect(AbstractDungeon.player.masterDeck, true)
            AbstractDungeon.effectsQueue.add(fx)
        }
    }

    override fun updateDescription() {
        description = formatDesc(0)
    }

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(ResearchAndDevPower::class.java)
    }
}