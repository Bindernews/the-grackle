package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.actions.FightFireAction
import io.bindernews.thegrackle.helper.BurnHelper
import io.bindernews.thegrackle.helper.ModInterop

class FightFire : BaseCard(C, VARS) {
    /** The owning creature, default is the player.  */
    var owner: AbstractCreature? = AbstractDungeon.player

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
        val discard = ModInterop.iop().getCardsByType(owner, CardGroup.CardGroupType.DISCARD_PILE)
        if (discard.isPresent) {
            burnCount = BurnHelper.countBurns(discard.get())
        }
        baseDamage = DAMAGE + burnCount * magicNumber
    }

    override fun use(p: AbstractPlayer, m: AbstractMonster) {
        addToBot(FightFireAction(p, m, DAMAGE, magicNumber))
    }

    override fun apply(p: AbstractCreature, m: AbstractCreature) {
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