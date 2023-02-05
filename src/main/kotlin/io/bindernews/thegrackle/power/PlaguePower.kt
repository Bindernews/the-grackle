package io.bindernews.thegrackle.power

import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.thegrackle.GrackleMod
import io.bindernews.thegrackle.helper.ModInterop.Companion.iop

class PlaguePower(owner: AbstractCreature, amount: Int) : BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, amount)
        loadRegion("ai")
        updateDescription()
    }

    override fun atStartOfTurn() {
        addToBot(iop().actionApplyPower(owner, owner, MultiHitPower.POWER_ID, amount))
    }

    override fun updateDescription() {
        description = formatDesc(0, amount)
    }

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(PlaguePower::class.java)
    }
}