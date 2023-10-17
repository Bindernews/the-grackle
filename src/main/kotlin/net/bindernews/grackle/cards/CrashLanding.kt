package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder
import net.bindernews.grackle.helper.speedBoost
import net.bindernews.grackle.power.SpeedPower

/**
 * Deal damage; Speed boost doubles damage.
 */
class CrashLanding : BaseCard(C, VARS) {
    override val descriptionSource get() = descriptionBuilder

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val fx = AttackEffect.SLASH_DIAGONAL
        SpeedPower.tryBoost(p, this)
        addToBot(DamageAction(m, DamageInfo(p, damage, damageType), fx))
    }

    override fun calculateCardDamage(mo: AbstractMonster?) {
        super.calculateCardDamage(mo)
        if (SpeedPower.canBoost(owner!!, this)) {
            damage *= 2
        }
        isDamageModified = damage != baseDamage
    }

    override fun isBonusActive(): Boolean = isBonusActive(owner!!)

    companion object {
        @JvmField val C = CardConfig("CrashLanding", CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY)
        val VARS = CardVariables().apply {
            cost(1, -1)
            damage(8, 11)
            speedBoost(6, 4)
        }

        val descriptionBuilder = DescriptionBuilder.create {
            when (lang) {
                else -> format("{Deal} !D! {damage}. NL {speed_boost} Double {damage}.")
            }
        }
    }
}