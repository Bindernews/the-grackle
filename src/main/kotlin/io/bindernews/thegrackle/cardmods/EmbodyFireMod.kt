package io.bindernews.thegrackle.cardmods

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.thegrackle.power.EmbodyFirePower

class EmbodyFireMod : AbstractDamageModifier() {
    override fun atDamageFinalGive(
        damage: Float,
        type: DamageType,
        target: AbstractCreature?,
        card: AbstractCard?
    ): Float {
        if (target == null) {
            return damage
        }
        return if (target.hasPower(EmbodyFirePower.POWER_ID)) { -damage } else { damage }
    }

    override fun makeCopy(): AbstractDamageModifier = EmbodyFireMod()
}