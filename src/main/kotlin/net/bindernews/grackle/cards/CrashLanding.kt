package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.stances.NeutralStance
import net.bindernews.grackle.cardmods.AloftDmgUnaffectedMod
import net.bindernews.grackle.cardmods.RequireStanceMod
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.ModInterop.Companion.iop

class CrashLanding : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val fx = AttackEffect.SLASH_DIAGONAL
        addToBot(DamageAction(m, DamageInfo(p, damage, damageType), fx))
        addToBot(iop().changeStance(p, NeutralStance.STANCE_ID))
    }

    companion object {
        @JvmField
        val C = CardConfig("CrashLanding", CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY)
        val VARS = CardVariables().apply {
            cost(1, -1)
            damage(8, 12)
            addModifier(RequireStanceMod())
            addModifier(AloftDmgUnaffectedMod())
        }
    }
}