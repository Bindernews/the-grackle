package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.actions.FightFireAction
import net.bindernews.grackle.helper.BurnHelper
import net.bindernews.grackle.helper.ModInterop.Companion.iop

class FightFire : BaseCard(C, VARS) {
    init {
        rawDescription = String.format(rawDescription, DAMAGE)
        initializeDescription()
    }

    override fun applyPowers() {
        updateBaseDamage()
        super.applyPowers()
        initializeDescription()
    }

    override fun calculateCardDamage(mo: AbstractMonster) {
        updateBaseDamage()
        super.calculateCardDamage(mo)
        initializeDescription()
    }

    private fun updateBaseDamage() {
        var burnCount = 0
        val discard = iop().getCardsByType(owner!!, CardGroup.CardGroupType.DISCARD_PILE)
        if (discard.isPresent) {
            burnCount = BurnHelper.countBurns(discard.get())
        }
        baseDamage = DAMAGE + burnCount * magicNumber
    }

    override fun use(p: AbstractPlayer, m: AbstractMonster) {
        addToBot(FightFireAction(p, m, DAMAGE, magicNumber))
    }

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        throwPlayerOnly()
    }

    companion object {
        @JvmField val C = CardConfig("FightFire", CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY)
        const val DAMAGE = 4
        val VARS = CardVariables.config { c ->
            c.cost(1, -1)
            c.damage(DAMAGE, -1)
            c.magic(4, 6)
        }
    }
}