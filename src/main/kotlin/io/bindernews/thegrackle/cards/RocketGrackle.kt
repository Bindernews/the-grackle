package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.VulnerablePower
import io.bindernews.bnsts.CardVariables

class RocketGrackle : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val fx = AttackEffect.BLUNT_HEAVY
        val isMonster = p is AbstractMonster
        addToBot(DamageAction(m, DamageInfo(p, damage, damageTypeForTurn), fx))
        addToBot(ApplyPowerAction(m, p, VulnerablePower(m, magicNumber, isMonster), magicNumber))
    }

    companion object {
        @JvmField val C = CardConfig("RocketGrackle", CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY)
        val VARS = CardVariables.config { c ->
            c.cost(1, -1)
            c.damage(8, 12)
            c.magic(1, 2)
        }
    }
}