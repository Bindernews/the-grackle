package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.power.BurningPower

class FireTouch : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(BurningPower.makeAction(p, m!!, magicNumber))
    }

    companion object {
        @JvmField val C = CardConfig("FireTouch", CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY)
        val VARS = CardVariables.config { c ->
            c.cost(1)
            c.magic(6, 10)
        }
    }
}