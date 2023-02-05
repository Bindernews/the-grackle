package net.bindernews.grackle.power

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.RepairPower
import net.bindernews.grackle.GrackleMod

class PhoenixFeatherPower(owner: AbstractCreature, amount: Int) : RepairPower(owner, amount) {
    init {
        ID = POWER_ID
    }

    companion object {
        val POWER_ID = GrackleMod.makeId(PhoenixFeatherPower::class.java)
    }
}