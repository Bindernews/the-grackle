package net.bindernews.grackle.relics

import basemod.abstracts.CustomRelic
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction
import com.megacrit.cardcrawl.cards.status.Burn
import com.megacrit.cardcrawl.core.EnergyManager
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import net.bindernews.grackle.helper.RelicHelper
import net.bindernews.grackle.helper.RelicHelper.relicStrings
import net.bindernews.grackle.helper.makeId

class SimmeringHeat : CustomRelic(ID, IMAGES[0], IMAGES[1], RelicTier.BOSS, LandingSound.MAGICAL) {
    init {
        description = relicStrings.DESCRIPTIONS[0]
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
        val IMAGES = RelicHelper.loadImages(ID)
    }
}