package io.bindernews.thegrackle.cards

import basemod.cardmods.ExhaustMod
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.helper.ModInterop.iop
import io.bindernews.thegrackle.power.BurningPower

class WildFire : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        iop().getEnemies(p).forEach {
            addToBot(BurningPower.makeAction(p, it, magicNumber))
        }
        iop().getFriends(p).forEach {
            addToBot(BurningPower.makeAction(p, it, magicNumber))
        }
    }
    companion object {
        @JvmStatic val C = CardConfig("WildFire", CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF)
        @JvmStatic val VARS = CardVariables().apply {
            cost(2)
            magic(8, 10)
            addModifier(ExhaustMod())
        }
    }
}