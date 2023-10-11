package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.cardmods.ExtraHitsMod
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.extraHits
import net.bindernews.grackle.helper.hits

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
            cost(0)
            damage(2, 4)
            hits(2, 1)
            addModifier(ExtraHitsMod())
        }
    }
}