package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder
import net.bindernews.grackle.helper.speedBoost

class AerialAce : BaseCard(C, VARS) {
    override val descriptionSource = DESCRIPTION_BUILDER

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(DamageAction(m, DamageInfo(p, damage, damageType), AttackEffect.SLASH_DIAGONAL))
    }

    override fun canPlay(card: AbstractCard?): Boolean {
        costForTurn = if (isBonusActive()) 0 else cost
        return super.canPlay(card)
    }

    override fun isBonusActive(): Boolean = isBonusActive(owner!!)

    companion object {
        @JvmField val C = CardConfig("AerialAce", CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY)
        val VARS = CardVariables().apply {
            cost(1)
            damage(8, 12)
            speedBoost(8, -1)
        }

        val DESCRIPTION_BUILDER = DescriptionBuilder.create {
            format("{Deal} !D! {damage}. NL {speed_boost} NL {+costs_n}.").format(0)
        }
    }
}