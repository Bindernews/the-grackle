package net.bindernews.grackle.power

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.variables.ExtraHitsVariable

class TempSpeedPower(owner: AbstractCreature, amount: Int) : BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, amount)
        type = PowerType.BUFF
        isTurnBased = true
        updateDescription()
        loadRegion("energized_blue");
    }

    override fun updateDescription() {
        description = formatDesc(0, amount)
    }

    override fun atEndOfTurn(isPlayer: Boolean) {
        if (isPlayer) {
            addToBot(RemoveSpecificPowerAction(owner, owner, this))
        }
    }

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(TempSpeedPower::class.java)

        init {
            ExtraHitsVariable.onApplyPowers.on(-5, ExtraHitsVariable.addPowerAmount(POWER_ID))
        }
    }
}