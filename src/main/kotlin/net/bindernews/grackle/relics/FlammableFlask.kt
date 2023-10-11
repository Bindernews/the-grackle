package net.bindernews.grackle.relics

import basemod.abstracts.CustomRelic
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.evacipated.cardcrawl.mod.stslib.relics.OnApplyPowerRelic
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.AbstractPower
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.helper.RelicHelper
import net.bindernews.grackle.helper.RelicHelper.relicStrings
import net.bindernews.grackle.helper.makeId
import net.bindernews.grackle.power.BurningPower

/**
 * Whenever [BurningPower] is applied, apply [EXTRA_AMOUNT] more.
 */
class FlammableFlask : CustomRelic(ID, IMAGES[0], IMAGES[1], RelicTier.COMMON, LandingSound.CLINK), OnApplyPowerRelic {
    override fun updateDescription(c: AbstractPlayer.PlayerClass?) {
        super.updateDescription(c)
        description = relicStrings.DESCRIPTIONS[0].format(EXTRA_AMOUNT)
    }

    override fun onApplyPower(toApply: AbstractPower, target: AbstractCreature, source: AbstractCreature): Boolean {
        if (toApply.ID == BurningPower.POWER_ID && target !== AbstractDungeon.player && source === AbstractDungeon.player) {
            toApply.amount += EXTRA_AMOUNT
        }
        return true
    }

    override fun render(sb: SpriteBatch?) {
        super.render(sb)
    }

    companion object {
        @JvmField val ID = makeId(FlammableFlask::class)
        val IMAGES = RelicHelper.loadImages(ID)
        @JvmStatic var EXTRA_AMOUNT = 1
    }
}