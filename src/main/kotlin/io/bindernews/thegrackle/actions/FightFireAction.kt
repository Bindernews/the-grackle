package io.bindernews.thegrackle.actions

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.thegrackle.helper.BurnHelper
import io.bindernews.thegrackle.helper.ModInterop

class FightFireAction(
    source: AbstractCreature, target: AbstractCreature, baseDmg: Int, damageMultiplier: Int
) : AbstractGameAction() {
    private val baseDmg: Int

    init {
        this.source = source
        this.target = target
        this.baseDmg = baseDmg
        amount = damageMultiplier
        attackEffect = AttackEffect.FIRE
    }

    override fun update() {
        isDone = true
        val discardPile = ModInterop.iop().getCardsByType(source, CardGroup.CardGroupType.DISCARD_PILE)
            .orElse(null)
        if (target != null && target.currentHealth > 0 && discardPile != null) {
            val dmg = baseDmg + BurnHelper.exhaustBurnsInGroup(discardPile) * amount
            addToTop(DamageAction(target, DamageInfo(source, dmg, damageType), attackEffect))
        }
    }
}