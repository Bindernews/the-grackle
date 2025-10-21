package net.bindernews.grackle.power

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.relics.LiminalSpaceRelic
import net.bindernews.grackle.variables.ExtraHitsVariable
import net.bindernews.grackle.variables.ExtraHitsVariable.Companion.addPowerAmount

class SpeedPower(owner: AbstractCreature, amount: Int) : BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, amount)
        type = PowerType.BUFF
        updateDescription()
        loadRegion("energized_blue");
    }

    override fun updateDescription() {
        description = formatDesc(0, amount)
    }

    override fun onInitialApplication() {
        val own = owner;
        if (own !is AbstractPlayer) return
        // If the player doesn't have the LiminalSpace relic, give it to them
        if (!own.relics.any { it.relicId == LiminalSpaceRelic.ID }) {
            LiminalSpaceRelic.giveToPlayer(own)
        }
    }

    companion object {
        @JvmField
        val POWER_ID = GrackleMod.makeId(SpeedPower::class.java)

        init {
            ExtraHitsVariable.onApplyPowers.on(-5, addPowerAmount(POWER_ID))
        }
    }
}