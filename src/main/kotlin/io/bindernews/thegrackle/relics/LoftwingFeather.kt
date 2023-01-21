package io.bindernews.thegrackle.relics

import basemod.abstracts.CustomRelic
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction
import io.bindernews.thegrackle.helper.RelicHelper
import io.bindernews.thegrackle.helper.makeId
import io.bindernews.thegrackle.stance.StanceAloft

class LoftwingFeather : CustomRelic(ID, IMAGES[0], IMAGES[1], RelicTier.BOSS, LandingSound.FLAT) {
    override fun onPlayerEndTurn() {
        addToBot(ChangeStanceAction(StanceAloft.STANCE_ID))
    }

    companion object {
        @JvmField val ID = makeId(LoftwingFeather::class)
        val IMAGES = RelicHelper.loadImages(ID)
    }
}