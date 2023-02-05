package net.bindernews.grackle.power

import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.GrackleMod

class EmbodyFirePower(owner: AbstractCreature, amount: Int) : BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, amount)
        loadRegion("flameBarrier")
        updateDescription()
    }

    override fun updateDescription() {
        description = formatDesc(0)
    }

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(EmbodyFirePower::class.java)
    }
}