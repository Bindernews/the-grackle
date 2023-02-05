package net.bindernews.grackle.power

import com.megacrit.cardcrawl.actions.common.ReducePowerAction
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.stances.AbstractStance
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.stance.StancePhoenix.Companion.isStance

class CoolingPhoenixPower(owner: AbstractCreature, amount: Int) : BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, amount)
        type = PowerType.DEBUFF
        isTurnBased = true
        loadRegion("frail")
        updateDescription()
    }

    override fun onChangeStance(oldStance: AbstractStance, newStance: AbstractStance) {
        if (isStance(newStance)) {
            addToTop(ChangeStanceAction(oldStance))
        }
    }

    override fun atEndOfRound() {
        addToBot(ReducePowerAction(owner, owner, POWER_ID, 1))
    }

    override fun updateDescription() {
        description = strings.DESCRIPTIONS[0]
    }

    override fun makeCopy(): AbstractPower {
        return CoolingPhoenixPower(owner, amount)
    }

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(CoolingPhoenixPower::class.java)
    }
}