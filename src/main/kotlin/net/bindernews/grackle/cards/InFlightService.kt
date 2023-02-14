package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.ObtainPotionAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.potions.RegenPotion
import com.megacrit.cardcrawl.powers.RegenPower
import net.bindernews.grackle.cardmods.AutoDescription
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.ModInterop.Companion.iop

class InFlightService : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        if (p is AbstractPlayer) {
            addToBot(ObtainPotionAction(RegenPotion()))
        } else {
            addToBot(iop().actionApplyPower(p, p, RegenPower.POWER_ID, magicNumber))
        }
    }

    companion object {
        @JvmStatic val C = CardConfig("InFlightService", CardType.SKILL, CardRarity.RARE, CardTarget.SELF)
        val VARS = CardVariables().apply {
            cost(2, 1)
            magic(5)
            onInit { it.exhaust = true }
            addModifier(AutoDescription())
        }
    }
}