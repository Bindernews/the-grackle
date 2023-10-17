package net.bindernews.grackle.power

import com.megacrit.cardcrawl.actions.common.ReducePowerAction
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.makeId

class SpeedPreservePower(owner: AbstractCreature, amount: Int) : BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, amount)
        type = PowerType.BUFF
        isTurnBased = true
        loadRegion("blur")
    }

    override fun atEndOfRound() {
        if (amount == 0) {
            addToBot(RemoveSpecificPowerAction(owner, owner, POWER_ID))
        } else {
            addToBot(ReducePowerAction(owner, owner, POWER_ID, 1))
        }
    }

    override fun updateDescription() {
        description = strings.DESCRIPTIONS[0]
    }

    companion object {
        @JvmField val POWER_ID = makeId(SpeedPreservePower::class)
    }
}