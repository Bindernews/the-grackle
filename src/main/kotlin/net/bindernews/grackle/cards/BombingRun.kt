package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.cardmods.ExtraHitsMod
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.helper.extraHits
import net.bindernews.grackle.helper.hits

class BombingRun : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
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