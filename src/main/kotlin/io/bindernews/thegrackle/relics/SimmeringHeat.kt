package io.bindernews.thegrackle.relics

import basemod.abstracts.CustomRelic
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction
import com.megacrit.cardcrawl.cards.status.Burn
import com.megacrit.cardcrawl.core.EnergyManager
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import io.bindernews.thegrackle.helper.RelicHelper
import io.bindernews.thegrackle.helper.makeId

class SimmeringHeat : CustomRelic(ID, "", RelicTier.BOSS, LandingSound.FLAT) {
    init {
        RelicHelper.loadImages(this)
    }

    override fun atTurnStartPostDraw() {
        addToBot(MakeTempCardInHandAction(Burn()))
    }

    override fun onEquip() {
        energyManager.energyMaster++
    }

    override fun onUnequip() {
        energyManager.energyMaster--
    }

    private val energyManager: EnergyManager
        get() = AbstractDungeon.player.energy

    companion object {
        @JvmField val ID = makeId(SimmeringHeat::class)
    }
}