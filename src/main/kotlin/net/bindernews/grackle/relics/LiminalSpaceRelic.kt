package net.bindernews.grackle.relics

import basemod.abstracts.CustomRelic
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.RelicLibrary
import com.megacrit.cardcrawl.relics.AbstractRelic
import net.bindernews.grackle.helper.RelicHelper
import net.bindernews.grackle.helper.makeId

class LiminalSpaceRelic : CustomRelic(ID, IMAGES[0], IMAGES[1], RelicTier.SPECIAL, LandingSound.MAGICAL),
    ClickableRelic {

    private var activated: Boolean = false

    override fun onRightClick() {
        TODO("onRightClick")
    }

    override fun onTrigger(target: AbstractCreature?) {
        super.onTrigger(target)
    }



    override fun checkTrigger(): Boolean = activated

    companion object {
        @JvmField val ID = makeId(LiminalSpaceRelic::class)
        @JvmField val IMAGES = RelicHelper.loadImages(ID)

        @JvmStatic
        fun giveToPlayer(player: AbstractPlayer): AbstractRelic {
            val relic = RelicLibrary.getRelic(ID).makeCopy()
            AbstractDungeon.getCurrRoom()
                .spawnRelicAndObtain((Settings.WIDTH / 2).toFloat(), (Settings.HEIGHT / 2).toFloat(), relic)
            // Move the relic to index 0 for convenience
            player.relics.remove(relic)
            player.relics.add(0, relic)
            return relic
        }
    }
}