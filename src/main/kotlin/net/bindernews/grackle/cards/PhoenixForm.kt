package net.bindernews.grackle.cards

import basemod.AutoAdd
import basemod.helpers.BaseModCardTags
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.power.HealingPhoenixPower

@AutoAdd.Ignore
class PhoenixForm : BaseCard(C, VARS) {
    override fun use(p: AbstractPlayer, m: AbstractMonster) {
        addToBot(ApplyPowerAction(p, p, HealingPhoenixPower(p, magicNumber), magicNumber))
    }

    companion object {
        @JvmField val C = CardConfig("PhoenixForm", CardType.POWER, CardRarity.RARE, CardTarget.SELF)
        val VARS = CardVariables.config { c ->
            c.cost(3, 2)
            c.magic(6, -1)
            c.tags(BaseModCardTags.FORM)
        }
    }
}