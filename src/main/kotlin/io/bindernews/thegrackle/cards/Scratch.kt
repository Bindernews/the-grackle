package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.bnsts.CardVariables

class Scratch : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature) {
        val fx = AttackEffect.SLASH_DIAGONAL
        addToBot(DamageAction(m, DamageInfo(p, damage, damageType), fx))
    }

    companion object {
        @JvmField val C = CardConfig("Scratch", CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY)
        val VARS = CardVariables.config { c: CardVariables ->
            c.cost(0, -1)
            c.damage(6, 10)
        }
    }
}