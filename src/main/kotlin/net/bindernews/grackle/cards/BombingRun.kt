package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.defect.ChannelAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.cardmods.ExtraHitsMod
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.helper.extraHits
import net.bindernews.grackle.helper.hits
import net.bindernews.grackle.orb.BombOrb

class BombingRun : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(DamageAction(m, DamageInfo(p, damage, damageTypeForTurn), AttackEffect.FIRE));
        for (i in 0 until magicNumber) {
            this.addToBot(ChannelAction(BombOrb()));
        }
    }

    companion object {
        @JvmField val C = CardConfig("BombingRun", CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY)
        @JvmField val VARS = CardVariables()
        init {
            VARS.cost(1, -1);
            VARS.magic(1, 2);
            VARS.damage(6, 8);
        }
    }
}