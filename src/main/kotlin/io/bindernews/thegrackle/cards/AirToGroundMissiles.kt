package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.cardmods.DoubleAloftDamageMod
import io.bindernews.thegrackle.cardmods.ExtraHitsMod
import io.bindernews.thegrackle.cardmods.RequireAloftMod
import io.bindernews.thegrackle.variables.ExtraHitsVariable

class AirToGroundMissiles : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature) {
        val fx = AttackEffect.SLASH_HORIZONTAL
        val hits = ExtraHitsVariable.inst.value(this)
        for (i in 0 until hits) {
            if (p is AbstractPlayer) {
                addToBot(DamageAllEnemiesAction(p, multiDamage, damageType, fx))
            } else {
                addToBot(DamageAction(m, DamageInfo(p, damage, damageType), fx))
            }
        }
    }

    companion object {
        @JvmStatic val C = CardConfig("AirToGroundMissiles", CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY)
        val VARS = CardVariables.config { c ->
            c.cost(2)
            c.damage(16, 20)
            c.add(ExtraHitsVariable.inst, 1, -1)
            c.multiDamage(true, true)
            c.addModifier(ExtraHitsMod())
            c.addModifier(RequireAloftMod())
            c.addModifier(DoubleAloftDamageMod())
        }
    }
}