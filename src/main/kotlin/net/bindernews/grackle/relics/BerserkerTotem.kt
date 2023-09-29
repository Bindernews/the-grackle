package net.bindernews.grackle.relics

import basemod.abstracts.CustomRelic
import com.megacrit.cardcrawl.characters.AbstractPlayer
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.helper.RelicHelper
import net.bindernews.grackle.helper.RelicHelper.relicStrings
import net.bindernews.grackle.helper.makeId
import net.bindernews.grackle.variables.ExtraHitsVariable

/**
 * Adds 1 extra hit to all multi-hit attacks.
 */
class BerserkerTotem : CustomRelic(ID, IMAGES[0], IMAGES[1], RelicTier.COMMON, LandingSound.SOLID) {
    init {
        updateDescription(null)
    }

    override fun updateDescription(c: AbstractPlayer.PlayerClass?) {
        description = relicStrings.DESCRIPTIONS[0]
    }

    companion object {
        @JvmField val ID = makeId(BerserkerTotem::class)
        val IMAGES = RelicHelper.loadImages(ID)
        init {
            ExtraHitsVariable.onApplyPowers.on(-4) {
                it.addCount(if (iop().hasRelic(it.source, ID)) 1 else 0)
            }
        }
    }
}