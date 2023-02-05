package io.bindernews.thegrackle.power

import com.megacrit.cardcrawl.actions.common.HealAction
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.thegrackle.GrackleMod

class HealingPhoenixPower(owner: AbstractCreature, heal: Int) : BasePower(POWER_ID) {
    init {
        setOwnerAmount(owner, heal)
        type = PowerType.BUFF
        updateDescription()
    }

    override fun updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1]
    }

    override fun atEndOfTurn(isPlayer: Boolean) {
        flashWithoutSound()
        addToTop(HealAction(owner, owner, amount))
    }

    companion object {
        @JvmField val POWER_ID = GrackleMod.makeId(HealingPhoenixPower::class.java)
    }
}