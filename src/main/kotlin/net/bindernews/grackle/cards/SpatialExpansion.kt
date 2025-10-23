package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables

/**
 * Expands weapon/orb slots like Defect's Capacitor.
 */
class SpatialExpansion : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(IncreaseMaxOrbAction(magicNumber))
    }

    companion object {
        @JvmField val C: CardConfig = CardConfig("SpatialExpansion", CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF)
        @JvmField val VARS: CardVariables = CardVariables().apply {
            cost(1, -1)
            magic(2, 3)
        }
    }
}
