package net.bindernews.grackle.power

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.makeId

class StanceAloftPower(owner: AbstractCreature, amount: Int) : BasePower(POWER_ID), InvisiblePower {
    init {
        @Suppress("UNUSED_VARIABLE") val ignore = amount
        setOwnerAmount(owner, -1)
        type = PowerType.BUFF
        isTurnBased = false
        // Higher priority goes later, I think?
        priority = 7
    }

    override fun modifyBlock(blockAmount: Float): Float {
        return blockAmount * 1.75f
    }

    companion object {
        @JvmField val POWER_ID = makeId(StanceAloftPower::class)
    }
}