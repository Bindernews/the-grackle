package net.bindernews.grackle.relics

import basemod.abstracts.CustomRelic
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction
import net.bindernews.grackle.helper.RelicHelper
import net.bindernews.grackle.helper.makeId
import net.bindernews.grackle.stance.StanceAloft

class LoftwingFeather : CustomRelic(ID, IMAGES[0], IMAGES[1], RelicTier.BOSS, LandingSound.FLAT) {
    override fun onPlayerEndTurn() {
        addToBot(ChangeStanceAction(StanceAloft.STANCE_ID))
    }

    companion object {
        @JvmField val ID = makeId(LoftwingFeather::class)
        val IMAGES = RelicHelper.loadImages(ID)
    }
}