package io.bindernews.thegrackle.relics

import basemod.abstracts.CustomRelic
import io.bindernews.thegrackle.helper.RelicHelper
import io.bindernews.thegrackle.helper.makeId

class PhoenixIdol : CustomRelic(ID, "", RelicTier.RARE, LandingSound.MAGICAL) {
    init {
        RelicHelper.loadImages(this)
    }

    companion object {
        @JvmField val ID = makeId(PhoenixIdol::class)
    }
}