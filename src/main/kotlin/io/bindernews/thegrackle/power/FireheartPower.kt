package io.bindernews.thegrackle.power

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.AbstractPower
import io.bindernews.thegrackle.GrackleMod
import io.bindernews.thegrackle.helper.ModInterop.Companion.iop
import io.bindernews.thegrackle.helper.fireheartGained
import io.bindernews.thegrackle.stance.StancePhoenix
import io.bindernews.thegrackle.stance.StancePhoenix.Companion.isStance

class FireheartPower(owner: AbstractCreature, amount: Int) : BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, amount)
        isTurnBased = true
        type = PowerType.BUFF
        owner.fireheartGained += amount
        loadRegion("flameBarrier")
        updateDescription()
    }

    override fun stackPower(stackAmount: Int) {
        super.stackPower(stackAmount)
        // Yes, this means you can build fireheart even while in stance.
        if (amount >= fireheartRequired && canEnterPhoenixStance()) {
            addToBot(RemoveSpecificPowerAction(owner, owner, this))
            addToBot(iop().changeStance(owner, StancePhoenix.STANCE_ID))
        }
    }

    fun canEnterPhoenixStance(): Boolean {
        return !isStance(iop().getStance(owner)) && !owner.hasPower(CoolingPhoenixPower.POWER_ID)
    }

    override fun makeCopy(): AbstractPower {
        return FireheartPower(owner, amount)
    }

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(FireheartPower::class.java)
        @JvmField var fireheartRequired = 10
    }
}