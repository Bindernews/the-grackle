package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.stances.NeutralStance
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.cardmods.*
import io.bindernews.thegrackle.helper.ModInterop.iop
import io.bindernews.thegrackle.helper.extraHits
import io.bindernews.thegrackle.helper.hits

/**
 * Crash-Landing, but better.
 */
class Paratrooper : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature) {
        val fx = AttackEffect.SLASH_DIAGONAL
        val hits = extraHits
        addToBot(iop().changeStance(p, NeutralStance.STANCE_ID))
        for (i in 0 until hits) {
            addToBot(DamageAction(m, DamageInfo(p, damage, damageTypeForTurn), fx))
        }
    }

    companion object {
        @JvmField val C = CardConfig("Paratrooper", CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY)
        val VARS = CardVariables.config { c ->
            c.cost(1, -1)
            c.damage(7, 12)
            c.hits(1, -1)
            c.addModifier(ExtraHitsMod())
            c.addModifier(RequireAloftMod())
            c.addModifier(AloftDmgUnaffectedMod())
        }
    }
}