package net.bindernews.grackle.power

import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.makeId
import net.bindernews.grackle.variables.ExtraHitsVariable

class PlaguePower(owner: AbstractCreature, amount: Int) : BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, amount)
        type = PowerType.BUFF
        loadRegion("ai")
        updateDescription()
    }

    override fun updateDescription() {
        description = formatDesc(0, amount)
    }

    companion object {
        @JvmField val POWER_ID = makeId(PlaguePower::class)

        init {
            ExtraHitsVariable.onApplyPowers.on(-5, ExtraHitsVariable.addPowerAmount(POWER_ID))
        }
    }
}