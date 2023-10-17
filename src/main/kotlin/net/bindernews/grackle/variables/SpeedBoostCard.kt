package net.bindernews.grackle.variables

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.power.SpeedPower

interface SpeedBoostCard {
    val speedBoost: VariableInst

    fun isBonusActive(c: AbstractCreature): Boolean = SpeedPower.canBoost(c, this as AbstractCard)
}