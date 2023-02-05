package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables

class AAA : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val fx = AttackEffect.SLASH_VERTICAL
        addToBot(DamageAction(m, DamageInfo(p, damage, damageType), fx))
    }

    companion object {
        @JvmStatic val C = CardConfig("AAA", CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY)
        val VARS = CardVariables().apply {
            cost(2)
            damage(14, 20)
        }
    }
}