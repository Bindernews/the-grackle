package io.bindernews.thegrackle.cards

import basemod.cardmods.ExhaustMod
import com.megacrit.cardcrawl.cards.status.Burn
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.helper.ModInterop.Companion.iop
import io.bindernews.thegrackle.helper.magic2
import io.bindernews.thegrackle.power.BurningPower

class WildFire : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val amt = magicNumber
        iop().getEnemies(p).forEach { addToBot(BurningPower.makeAction(p, it, amt)) }
//        iop().getFriends(p).forEach { addToBot(BurningPower.makeAction(p, it, amt)) }
        addToBot(iop().actionMakeTempCardInDiscard(p, Burn(), magic2))
    }

    companion object {
        @JvmStatic val C = CardConfig("WildFire", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        @JvmStatic val VARS = CardVariables().apply {
            cost(2)
            magic(8, 10)
            magic2(2, -1)
            onInit { it.cardsToPreview = Burn() }
            addModifier(ExhaustMod())
        }
    }
}