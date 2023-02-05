package net.bindernews.grackle.cardmods

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier
import com.megacrit.cardcrawl.actions.common.HealAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.power.EmbodyFirePower

class EmbodyFireMod : AbstractDamageModifier() {
    override fun onAttackToChangeDamage(info: DamageInfo, damageAmount: Int, target: AbstractCreature): Int {
        return if (target.hasPower(EmbodyFirePower.POWER_ID)) {
            addToBot(HealAction(target, target, damageAmount))
            0
        } else {
            damageAmount
        }
    }

    override fun ignoresBlock(target: AbstractCreature): Boolean {
        return target.hasPower(EmbodyFirePower.POWER_ID)
    }

    override fun affectsDamageType(type: DamageType): Boolean {
        return (type == DamageType.THORNS)
    }

    override fun makeCopy(): AbstractDamageModifier = EmbodyFireMod()
}