package net.bindernews.grackle.power

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.watcher.VigorPower
import net.bindernews.grackle.GrackleMod

class TargetingComputerPower(owner: AbstractCreature, amount: Int) : BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, amount)
        loadRegion("lockon")
    }

    override fun atStartOfTurn() {
        addToBot(ApplyPowerAction(owner, owner, VigorPower(owner, amount)))
    }

    override fun updateDescription() {
        description = formatDesc(0, amount)
    }

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(TargetingComputerPower::class.java)
    }
}