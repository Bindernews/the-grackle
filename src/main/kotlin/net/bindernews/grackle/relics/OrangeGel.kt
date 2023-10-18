package net.bindernews.grackle.relics

import basemod.abstracts.CustomRelic
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.helper.RelicHelper
import net.bindernews.grackle.helper.makeId
import net.bindernews.grackle.power.SpeedPower

/**
 * Gain 2 Speed at the start of each turn.
 */
class OrangeGel : CustomRelic(ID, IMAGES[0], IMAGES[1], RelicTier.STARTER, LandingSound.MAGICAL) {

    override fun atTurnStartPostDraw() {
        val owner = AbstractDungeon.player
        addToBot(iop().actionApplyPower(owner, owner, SpeedPower.POWER_ID, bonusAmount))
    }

    companion object {
        @JvmField val ID = makeId(OrangeGel::class)
        @JvmField val IMAGES = RelicHelper.loadImages(ID)
        @JvmField var bonusAmount = 2
    }
}