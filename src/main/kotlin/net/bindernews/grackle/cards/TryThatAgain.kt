package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.unique.CalculatedGambleAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import net.bindernews.grackle.helper.CardVariables

class TryThatAgain : BaseCard(C, VARS) {
    override fun use(p: AbstractPlayer, m: AbstractMonster) {
        addToBot(CalculatedGambleAction(true))
    }

    companion object {
        @JvmField val C = CardConfig("TryThatAgain", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE)
        val VARS = CardVariables.config { c -> c.cost(1, 0) }
    }
}