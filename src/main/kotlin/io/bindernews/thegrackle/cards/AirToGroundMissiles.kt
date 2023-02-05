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
import io.bindernews.thegrackle.helper.extraHits
import io.bindernews.thegrackle.helper.hits

class AirToGroundMissiles : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val fx = AttackEffect.SLASH_HORIZONTAL
        val hits = extraHits
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
        val VARS = CardVariables().apply {
            cost(2)
            damage(8, 14)
            hits(1, -1)
            multiDamage(true, true)
            addModifier(ExtraHitsMod())
            addModifier(RequireAloftMod())
            addModifier(DoubleAloftDamageMod())
        }
    }
}