package io.bindernews.thegrackle.relics

import basemod.abstracts.CustomRelic
import io.bindernews.thegrackle.helper.makeId

class PhoenixIdol : CustomRelic(ID, "", RelicTier.RARE, LandingSound.MAGICAL) {
    companion object {
        @JvmField val ID = makeId(PhoenixIdol::class)
    }
}