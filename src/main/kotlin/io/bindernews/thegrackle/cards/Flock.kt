package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.actions.AddHitsAction
import io.bindernews.thegrackle.cardmods.ExtraHitsMod
import io.bindernews.thegrackle.helper.extraHits
import io.bindernews.thegrackle.helper.hits

class Flock : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val fx = AttackEffect.BLUNT_LIGHT
        val hits = extraHits
        for (i in 0 until hits) {
            addToBot(DamageAction(m, DamageInfo(p, damage, damageTypeForTurn), fx))
        }
        addToBot(AddHitsAction(this, magicNumber, AddHitsAction.getPlayerCardGroups()))
    }

    companion object {
        @JvmField val C = CardConfig("Flock", CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY)
        val VARS = CardVariables().apply {
            cost(1, -1)
            damage(2, 4)
            magic(1, -1)
            hits(1, 2)
            addModifier(ExtraHitsMod())
        }
    }
}