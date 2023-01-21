package io.bindernews.thegrackle.relics

import basemod.abstracts.CustomRelic
import io.bindernews.thegrackle.helper.ModInterop.iop
import io.bindernews.thegrackle.helper.RelicHelper
import io.bindernews.thegrackle.helper.makeId
import io.bindernews.thegrackle.variables.ExtraHitsVariable

class BerserkerTotem : CustomRelic(ID, "", RelicTier.COMMON, LandingSound.SOLID) {
    init {
        RelicHelper.loadImages(this)
    }

    companion object {
        @JvmField val ID = makeId(BerserkerTotem::class)
        init {
            ExtraHitsVariable.getOnApplyPowers().on(-4) {
                it.addCount(if (iop().hasRelic(it.source, ID)) 1 else 0)
            }
        }
    }
}