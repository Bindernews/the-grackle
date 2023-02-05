package net.bindernews.grackle.relics

import basemod.abstracts.CustomRelic
import net.bindernews.grackle.helper.makeId

class PhoenixIdol : CustomRelic(ID, "", RelicTier.RARE, LandingSound.MAGICAL) {
    companion object {
        @JvmField val ID = makeId(PhoenixIdol::class)
    }
}