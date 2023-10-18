package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import net.bindernews.grackle.cardmods.ExtraHitsMod
import net.bindernews.grackle.helper.*
import net.bindernews.grackle.power.SpeedPower

class AirRaid : BaseCard(C, VARS) {
    override val descriptionSource get() = DESCRIPTION_BUILDER

    init {
        damageType = DamageInfo.DamageType.NORMAL
        damageTypeForTurn = damageType
    }

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        SpeedPower.tryBoost(p, this)
        val fx = AttackEffect.BLUNT_HEAVY
        val hits = extraHits
        for (i in 0 until hits) {
            addToBot(DamageAction(m, DamageInfo(p, damage, damageTypeForTurn), fx))
        }
    }

    override fun calculateCardDamage(mo: AbstractMonster?) {
        baseExtraHits = BASE_HITS + if (isBonusActive()) BOOST_HITS else 0
        extraHits = baseExtraHits
        super.calculateCardDamage(mo)
    }

    override fun isBonusActive(): Boolean = isBonusActive(owner!!)

    companion object {
        @JvmField val C = CardConfig("AirRaid", CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY)
        const val BASE_HITS = 1
        const val BOOST_HITS = 2
        val VARS = CardVariables.config { c: CardVariables ->
            c.cost(2, -1)
            c.damage(8, 12)
            c.hits(BASE_HITS, -1)
            c.speedBoost(20, 16)
            c.addModifier(ExtraHitsMod())
        }

        val DESCRIPTION_BUILDER = DescriptionBuilder.create {
            format("{Deal} !D! {damage} {v_hits} {times}. NL {speed_boost} NL +$BOOST_HITS {extra} {hits}.")
        }
    }
}
