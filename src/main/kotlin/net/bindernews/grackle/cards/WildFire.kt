package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.cards.status.Burn
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.cardmods.AutoDescription
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.helper.magic2
import net.bindernews.grackle.power.BurningPower

class WildFire : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val amt = magicNumber
        iop().getEnemies(p).forEach { addToBot(BurningPower.makeAction(p, it, amt)) }
//        iop().getFriends(p).forEach { addToBot(BurningPower.makeAction(p, it, amt)) }
        addToBot(iop().actionMakeTempCardInDiscard(p, Burn(), magic2))
    }

    companion object {
        @JvmField val C = CardConfig("WildFire", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        @JvmField val VARS = CardVariables().apply {
            cost(2)
            magic(8, 10)
            magic2(2, -1)
            onInit { it.cardsToPreview = Burn(); it.exhaust = true }
            addModifier(AutoDescription())
        }
    }
}