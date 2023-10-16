package net.bindernews.grackle.power

import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.helper.makeId

class SpeedFormPower(owner: AbstractCreature, amount: Int): BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, amount)
        type = PowerType.BUFF
        updateDescription()
        loadRegion("demonForm")
    }

    override fun atStartOfTurnPostDraw() {
        flash();
        addToBot(iop().actionApplyPower(owner, owner, SpeedPower.POWER_ID, amount));
    }

    companion object {
        @JvmField val POWER_ID = makeId(SpeedFormPower::class)
    }
}