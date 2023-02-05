package net.bindernews.grackle.power

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.AbstractPower
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.helper.fireheartGained
import net.bindernews.grackle.stance.StancePhoenix
import net.bindernews.grackle.stance.StancePhoenix.Companion.isStance

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