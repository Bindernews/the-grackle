package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.cardmods.ExtraHitsMod
import io.bindernews.thegrackle.cardmods.SuplexMod
import io.bindernews.thegrackle.helper.hits
import io.bindernews.thegrackle.power.MultiHitPower

class Suplex : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val dmgOther = DamageInfo(p, damage, damageTypeForTurn)
        val dmgSelf = DamageInfo(p, baseDamage, DamageInfo.DamageType.THORNS)
        addToBot(DamageAction(m, dmgOther, AttackEffect.BLUNT_HEAVY))
        addToBot(DamageAction(p, dmgSelf, AttackEffect.BLUNT_LIGHT))
    }

    companion object {
        @JvmStatic val C = CardConfig("Suplex", CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY)
        val VARS = CardVariables().apply {
            cost(2)
            magic(4, 6)
            damage(4, -1)
            hits(1, -1)
            tags(MultiHitPower.GK_MULTI_HIT_PRESERVE)
            addModifier(ExtraHitsMod())
            addModifier(SuplexMod())
        }
    }
}