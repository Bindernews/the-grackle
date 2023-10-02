package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.cardmods.ExtraHitsMod
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.extraHits
import net.bindernews.grackle.helper.hits

class DoubleKick : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val hits = extraHits
        for (i in 0 until hits) {
            addToBot(DamageAction(m, DamageInfo(p, damage, damageTypeForTurn), AttackEffect.SLASH_HORIZONTAL))
        }
    }

    companion object {
        @JvmField var C = CardConfig("DoubleKick", CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY)
        var VARS = CardVariables().apply {
            cost(1)
            damage(6, 9)
            hits(2)
            addModifier(ExtraHitsMod())
        }
    }
}