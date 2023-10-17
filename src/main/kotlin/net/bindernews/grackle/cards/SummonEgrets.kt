package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.unique.WhirlwindAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import net.bindernews.grackle.cardmods.ExtraHitsMod
import net.bindernews.grackle.helper.CardVariables

class SummonEgrets : BaseCard(C, VARS) {
    override fun use(p: AbstractPlayer, m: AbstractMonster) {
        addToBot(WhirlwindAction(p, multiDamage, DamageInfo.DamageType.NORMAL, freeToPlayOnce, -1))
    }

    companion object {
        @JvmField val C = CardConfig("SummonEgrets", CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY)
        val VARS = CardVariables.config { c: CardVariables ->
            c.cost(-1)
            c.damage(5, 8)
            c.addModifier(ExtraHitsMod())
            c.multiDamage(true, true)
        }
    }
}