package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.stances.NeutralStance
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.cardmods.AloftDmgUnaffectedMod
import net.bindernews.grackle.cardmods.ExtraHitsMod
import net.bindernews.grackle.cardmods.RequireStanceMod
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.helper.extraHits
import net.bindernews.grackle.helper.hits

/**
 * Crash-Landing, but better.
 */
class Paratrooper : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
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
            c.addModifier(RequireStanceMod())
            c.addModifier(AloftDmgUnaffectedMod())
        }
    }
}