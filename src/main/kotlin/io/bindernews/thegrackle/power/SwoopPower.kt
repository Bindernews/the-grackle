package io.bindernews.thegrackle.power

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.thegrackle.GrackleMod
import io.bindernews.thegrackle.helper.ModInterop.Companion.iop
import io.bindernews.thegrackle.stance.StanceAloft

class SwoopPower(owner: AbstractCreature, @Suppress("UNUSED_PARAMETER") amount: Int) : BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, -1)
        type = PowerType.BUFF
        isTurnBased = true
        loadRegion("anger")
        updateDescription()
    }

    override fun atEndOfTurn(isPlayer: Boolean) {
        addToBot(iop().changeStance(owner, StanceAloft.STANCE_ID))
        addToBot(RemoveSpecificPowerAction(owner, owner, this))
    }

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(SwoopPower::class.java)
    }
}