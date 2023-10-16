package net.bindernews.grackle.power

import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.GrackleMod
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

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(SpeedPower::class.java)

        init {
            ExtraHitsVariable.onApplyPowers.on(-5, addPowerAmount(POWER_ID))
        }
    }
}