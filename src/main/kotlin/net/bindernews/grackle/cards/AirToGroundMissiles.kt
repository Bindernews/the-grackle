package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.cardmods.DoubleAloftDamageMod
import net.bindernews.grackle.cardmods.ExtraHitsMod
import net.bindernews.grackle.cardmods.RequireStanceMod
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.extraHits
import net.bindernews.grackle.helper.hits

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
            addModifier(RequireStanceMod())
            addModifier(DoubleAloftDamageMod())
        }
    }
}