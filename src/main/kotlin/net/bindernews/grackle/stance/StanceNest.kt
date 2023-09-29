package net.bindernews.grackle.stance

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.TeardropLocket
import com.megacrit.cardcrawl.stances.AbstractStance
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.helper.ModInterop.Companion.iop

/**
 * On entering the stance, gain 2 energy.
 */
class StanceNest : AbstractStance(), StanceDelegate {
    var owner: AbstractCreature = AbstractDungeon.player

    override fun updateDescription() {
        description = STRINGS.DESCRIPTION[0].format(getEnergyRestored())
    }

    override fun getDescription(): String = description

    override fun onEnterStance() {
        iop().getEnergy(owner)?.let { it.energy += getEnergyRestored() }
    }

    private fun getEnergyRestored(): Int {
        val owner = this.owner
        var amt = BASE_ENERGY_GAIN
        if (iop().hasRelic(owner, TeardropLocket.ID)) {
            amt += 1
        }
        return amt
    }

    companion object {
        @JvmStatic val STANCE_ID = GrackleMod.makeId("StanceNest")
        @JvmStatic val STRINGS = CardCrawlGame.languagePack.getStanceString(STANCE_ID)!!

        /**
         * How much energy is restored.
         * Other mods can change this, I guess.
         */
        var BASE_ENERGY_GAIN = 2
    }
}