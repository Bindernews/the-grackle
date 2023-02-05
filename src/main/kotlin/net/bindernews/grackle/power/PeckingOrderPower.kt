package net.bindernews.grackle.power

import com.megacrit.cardcrawl.actions.common.ReducePowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.variables.ExtraHitsVariable
import net.bindernews.grackle.variables.ExtraHitsVariable.Companion.addPowerAmount

class PeckingOrderPower(owner: AbstractCreature, amount: Int) : BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, amount)
        isTurnBased = true
        loadRegion("ai")
        updateDescription()
    }

    override fun atEndOfRound() {
        addToBot(ReducePowerAction(owner, owner, this, 1))
    }

    override fun updateDescription() {
        description = formatDesc(0, amount)
    }

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(PeckingOrderPower::class.java)

        init {
            ExtraHitsVariable.onApplyPowers.on(-5, addPowerAmount(POWER_ID))
        }
    }
}