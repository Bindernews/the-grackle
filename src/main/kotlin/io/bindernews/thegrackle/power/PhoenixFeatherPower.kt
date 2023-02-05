package io.bindernews.thegrackle.power

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.RepairPower
import io.bindernews.thegrackle.GrackleMod

class PhoenixFeatherPower(owner: AbstractCreature, amount: Int) : RepairPower(owner, amount) {
    init {
        ID = POWER_ID
    }

    companion object {
        val POWER_ID = GrackleMod.makeId(PhoenixFeatherPower::class.java)
    }
}