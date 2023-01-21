package io.bindernews.thegrackle.relics

import basemod.abstracts.CustomRelic
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction
import io.bindernews.thegrackle.helper.RelicHelper
import io.bindernews.thegrackle.helper.makeId
import io.bindernews.thegrackle.stance.StanceAloft

class LoftwingFeather : CustomRelic(ID, "", RelicTier.BOSS, LandingSound.FLAT) {
    init {
        RelicHelper.loadImages(this)
    }

    override fun onPlayerEndTurn() {
        addToBot(ChangeStanceAction(StanceAloft.STANCE_ID))
    }

    companion object {
        @JvmField val ID = makeId(LoftwingFeather::class)
    }
}