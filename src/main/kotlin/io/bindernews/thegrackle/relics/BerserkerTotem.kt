package io.bindernews.thegrackle.relics

import basemod.abstracts.CustomRelic
import com.megacrit.cardcrawl.characters.AbstractPlayer
import io.bindernews.thegrackle.helper.ModInterop.iop
import io.bindernews.thegrackle.helper.RelicHelper
import io.bindernews.thegrackle.helper.RelicHelper.relicStrings
import io.bindernews.thegrackle.helper.makeId
import io.bindernews.thegrackle.variables.ExtraHitsVariable

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
            ExtraHitsVariable.getOnApplyPowers().on(-4) {
                it.addCount(if (iop().hasRelic(it.source, ID)) 1 else 0)
            }
        }
    }
}