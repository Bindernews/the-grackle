package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.cardmods.ExtraHitsMod
import io.bindernews.thegrackle.helper.ModInterop.iop
import io.bindernews.thegrackle.helper.extraHits
import io.bindernews.thegrackle.helper.hits

class BombingRun : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature) {
        val fx = AttackEffect.BLUNT_HEAVY
        val hits = extraHits
        for (i in 0 until hits) {
            addToBot(iop().damageAllEnemies(p, multiDamage, damageTypeForTurn, fx))
        }
    }

    companion object {
        @JvmField val C = CardConfig("BombingRun", CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY)
        val VARS = CardVariables.config { c ->
            c.cost(1)
            c.damage(6, 10)
            c.hits(1, -1)
            c.addModifier(ExtraHitsMod())
            c.multiDamage(true, true)
        }
    }
}