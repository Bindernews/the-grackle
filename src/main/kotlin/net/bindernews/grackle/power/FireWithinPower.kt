package net.bindernews.grackle.power

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.GrackleMod

class FireWithinPower(owner: AbstractCreature, amount: Int) : BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, amount)
        loadRegion("devotion")
    }

    override fun atStartOfTurn() {
        addToBot(ApplyPowerAction(owner, owner, FireheartPower(owner, amount), amount, true))
    }

    override fun updateDescription() {
        description = formatDesc(0, amount)
    }

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(FireWithinPower::class.java)
    }
}