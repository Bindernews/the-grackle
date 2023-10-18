package net.bindernews.grackle.relics

import basemod.abstracts.CustomRelic
import basemod.cardmods.EtherealMod
import basemod.cardmods.ExhaustMod
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction
import com.megacrit.cardcrawl.cards.status.Burn
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.EnergyManager
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import net.bindernews.grackle.helper.RelicHelper
import net.bindernews.grackle.helper.RelicHelper.relicStrings
import net.bindernews.grackle.helper.addModifier
import net.bindernews.grackle.helper.makeId

/**
 * Energy relic that adds ethereal burns to your discard.
 */
class SimmeringHeat : CustomRelic(ID, IMAGES[0], IMAGES[1], RelicTier.BOSS, LandingSound.MAGICAL) {

    override fun getUpdatedDescription(): String = relicStrings.DESCRIPTIONS[0]

    override fun atTurnStartPostDraw() {
        val card = Burn()
        card.addModifier(ExhaustMod())
        addToBot(MakeTempCardInDiscardAction(card, 1))
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