package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.cardmods.AutoDescription
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.power.BurningPower

class FireControl : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        if (m != null) {
            val amt = (m.getPower(BurningPower.POWER_ID)?.amount ?: 0) * magicNumber
            addToBot(BurningPower.makeAction(p, m, amt))
        }
    }

    companion object {
        @JvmField val C = CardConfig("FireControl", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY)
        val VARS = CardVariables().apply {
            cost(1)
            magic(1, 2)
            onInit {
                it.exhaust = true
            }
            onUpgrade {
                it.rawDescription = C.strings.UPGRADE_DESCRIPTION
            }
            addModifier(AutoDescription())
        }
    }
}