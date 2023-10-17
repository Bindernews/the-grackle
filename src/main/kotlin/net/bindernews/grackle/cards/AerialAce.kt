package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.cardmods.AloftDmgUnaffectedMod
import net.bindernews.grackle.cardmods.DoubleAloftDamageMod
import net.bindernews.grackle.helper.CardVariables

class AerialAce : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(DamageAction(m, DamageInfo(p, damage, damageType), AttackEffect.SLASH_DIAGONAL))
    }

    companion object {
        @JvmStatic val C = CardConfig("AerialAce", CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY)
        val VARS = CardVariables().apply {
            cost(1)
            damage(8, 12)
            addModifier(AloftDmgUnaffectedMod())
            addModifier(DoubleAloftDamageMod())
        }
    }
}