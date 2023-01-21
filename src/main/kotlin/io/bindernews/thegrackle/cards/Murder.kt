package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.bnsts.CardVariables

class Murder : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(DamageAction(m, DamageInfo(p, damage, damageTypeForTurn), AttackEffect.BLUNT_HEAVY))
    }

    companion object {
        @JvmStatic val C = CardConfig("Murder", CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY)
        @JvmStatic val VARS = CardVariables().apply {
            cost(1)
            damage(20, 30)
            onInit {
                it.isInnate = true
                it.exhaust = true
            }
        }
    }
}