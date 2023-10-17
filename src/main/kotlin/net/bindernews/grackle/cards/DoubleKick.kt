package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import net.bindernews.grackle.cardmods.ExtraHitsMod
import net.bindernews.grackle.helper.*
import net.bindernews.grackle.power.SpeedPower

class DoubleKick : BaseCard(C, VARS) {
    override val descriptionSource get() = descriptionBuilder

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val hits = extraHits
        for (i in 0 until hits) {
            addToBot(DamageAction(m, DamageInfo(p, damage, damageTypeForTurn), AttackEffect.SLASH_HORIZONTAL))
        }
    }

    override fun calculateCardDamage(mo: AbstractMonster?) {
        baseExtraHits = BASE_HITS + if (isBonusActive()) EXTRA_HITS else 0
        super.calculateCardDamage(mo)
    }

    override fun isBonusActive(): Boolean = isBonusActive(owner!!)

    companion object {
        @JvmField var C = CardConfig("DoubleKick", CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY)
        const val BASE_HITS = 2
        const val EXTRA_HITS = 1
        val VARS = CardVariables().apply {
            cost(1)
            damage(6, 9)
            hits(BASE_HITS, -1)
            speedBoost(8, 6)
            addModifier(ExtraHitsMod())
        }

        val descriptionBuilder = DescriptionBuilder.create {
            format("{Deal} !D! {damage} {v_hits} {times}. NL {speed_boost} NL +1 extra hit.")
        }
    }
}