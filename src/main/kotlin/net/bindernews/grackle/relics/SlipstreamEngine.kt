package net.bindernews.grackle.relics

import basemod.abstracts.CustomRelic
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.helper.RelicHelper
import net.bindernews.grackle.helper.RelicHelper.relicStrings
import net.bindernews.grackle.helper.makeId
import net.bindernews.grackle.power.SpeedPower

/**
 * Gain 1 speed when you play a skill.
 */
class SlipstreamEngine : CustomRelic(ID, IMAGES[0], IMAGES[1], RelicTier.STARTER, LandingSound.HEAVY) {
    init {
        description = relicStrings.DESCRIPTIONS[0]
    }

    override fun onPlayCard(c: AbstractCard, m: AbstractMonster?) {
        if (c.type == AbstractCard.CardType.SKILL) {
            val owner = AbstractDungeon.player
            addToBot(iop().actionApplyPower(owner, owner, SpeedPower.POWER_ID, bonusAmount))
        }
    }

    companion object {
        @JvmField val ID = makeId(SlipstreamEngine::class)
        val IMAGES = RelicHelper.loadImages(ID)
        var bonusAmount = 1
    }
}