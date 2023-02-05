package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.EvolvePower
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.power.BurnCreamPower

class BurnCream : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(ApplyPowerAction(p, p, BurnCreamPower(p, -1), -1))
        if (upgraded) {
            addToBot(ApplyPowerAction(p, p, EvolvePower(p, 1)))
        }
    }

    override fun initializeDescription() {
        val st = C.strings
        rawDescription = st.DESCRIPTION
        if (upgraded) {
            rawDescription += st.EXTENDED_DESCRIPTION[0]
        }
        super.initializeDescription()
    }

    companion object {
        @JvmField val C = CardConfig("BurnCream", CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF)
        val VARS = CardVariables.config { c -> c.cost(1, -1) }
    }
}