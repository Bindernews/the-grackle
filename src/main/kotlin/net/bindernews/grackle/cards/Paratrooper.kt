package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import net.bindernews.grackle.cardmods.ExtraHitsMod
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder
import net.bindernews.grackle.helper.extraHits
import net.bindernews.grackle.helper.hits
import net.bindernews.grackle.power.SpeedPower

/**
 * Crash-Landing, but better.
 */
class Paratrooper : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val fx = AttackEffect.SLASH_DIAGONAL
        SpeedPower.tryBoost(p, this)
        val hits = extraHits
        for (i in 0 until hits) {
            addToBot(DamageAction(m, DamageInfo(p, damage, damageTypeForTurn), fx))
        }
    }

    override fun calculateCardDamage(mo: AbstractMonster?) {
        super.calculateCardDamage(mo)
        if (isBonusActive()) {
            extraHits += BOOST_EXTRA_HITS
        }
    }

    override fun isBonusActive(): Boolean = isBonusActive(owner!!)

    companion object {
        @JvmField val C = CardConfig("Paratrooper", CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY)
        const val BOOST_EXTRA_HITS = 2
        val VARS = CardVariables.config { c ->
            c.cost(1, -1)
            c.damage(7, 12)
            c.hits(1, -1)
            c.addModifier(ExtraHitsMod())
        }

        val descriptionBuilder = DescriptionBuilder.create {
            format("{Deal} !D! {damage} {v_hits} {times}. NL {speed_boost} NL +$BOOST_EXTRA_HITS hits.")
        }
    }
}