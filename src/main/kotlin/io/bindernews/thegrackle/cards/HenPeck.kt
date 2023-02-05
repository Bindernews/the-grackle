package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.thegrackle.helper.CardVariables
import io.bindernews.thegrackle.cardmods.ExtraHitsMod
import io.bindernews.thegrackle.helper.extraHits
import io.bindernews.thegrackle.helper.hits

class HenPeck : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val fx = AttackEffect.BLUNT_LIGHT
        val hits = extraHits
        for (i in 0 until hits) {
            addToBot(DamageAction(m, DamageInfo(p, damage, damageType), fx))
        }
    }
    companion object {
        @JvmStatic val C = CardConfig("HenPeck", CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY)
        @JvmStatic val VARS = CardVariables().apply {
            cost(1)
            damage(4, -1)
            hits(3, 4)
            addModifier(ExtraHitsMod())
        }
    }
}