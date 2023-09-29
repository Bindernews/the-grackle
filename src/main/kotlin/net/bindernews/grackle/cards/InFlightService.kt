package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.ObtainPotionAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.potions.PotionSlot
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

    override fun canUse(p: AbstractPlayer, m: AbstractMonster?): Boolean {
        return super.canUse(p, m) && p.potions.any { it is PotionSlot }
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