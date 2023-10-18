package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.DiscardAction
import com.megacrit.cardcrawl.actions.common.DrawCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.DescriptionBuilder
import net.bindernews.grackle.helper.speedBoost
import net.bindernews.grackle.power.SpeedPower

class Forage : BaseCard(C, VARS) {
    override val descriptionSource get() = DESCRIPTION_BUILDER

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        SpeedPower.tryBoost(p, this)
        addToBot(DrawCardAction(p, magicNumber))
        addToBot(DiscardAction(p, p, 1, false))
    }

    override fun canPlay(card: AbstractCard?): Boolean {
        costForTurn = if (isBonusActive()) 0 else cost
        return super.canPlay(card)
    }

    override fun isBonusActive(): Boolean = isBonusActive(owner!!)

    companion object {
        @JvmField val C = CardConfig("Forage", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF)
        val VARS = CardVariables().apply {
            cost(1)
            magic(3, 4)
            speedBoost(4, -1)
        }

        val DESCRIPTION_BUILDER = DescriptionBuilder.create {
            format("{Draw} !M! {cards}. NL {+discard} 1 {card}. NL {speed_boost} NL {+costs_n:0}.", 0)
        }
    }
}