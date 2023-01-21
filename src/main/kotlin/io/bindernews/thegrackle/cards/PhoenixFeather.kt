package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.RepairPower
import io.bindernews.bnsts.CardVariables

/**
 * It's Self-Repair.
 */
class PhoenixFeather : BaseCard(C, VARS) {
    override fun use(p: AbstractPlayer, m: AbstractMonster) {
        addToBot(ApplyPowerAction(p, p, RepairPower(p, magicNumber), magicNumber))
    }

    override fun apply(p: AbstractCreature, m: AbstractCreature) {
        throwPlayerOnly()
    }

    companion object {
        @JvmField val C = CardConfig("PhoenixFeather", CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF)
        val VARS = CardVariables.config { c ->
            c.cost(1, -1)
            c.magic(7, 10)
            c.tags(CardTags.HEALING)
        }
    }
}