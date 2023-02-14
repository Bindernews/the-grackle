package net.bindernews.grackle.cards

import basemod.cardmods.ExhaustMod
import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.actions.animations.VFXAction
import com.megacrit.cardcrawl.actions.common.InstantKillAction
import com.megacrit.cardcrawl.actions.utility.WaitAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.vfx.combat.GiantTextEffect
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect
import net.bindernews.grackle.cardmods.RequireStanceMod
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.stance.StanceAloft

class Death : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        if (m != null) {
            addToBot(VFXAction(WeightyImpactEffect(m.hb.cX, m.hb.cY, Color.GOLD.cpy())))
            addToBot(WaitAction(0.8F))
            addToBot(VFXAction(GiantTextEffect(m.hb.cX, m.hb.cY)))

            if (!m.isDeadOrEscaped && m.currentHealth <= magicNumber) {
                addToBot(InstantKillAction(m))
            }
        }
    }

    override fun canUse(p: AbstractPlayer, m: AbstractMonster?): Boolean {
        return StanceAloft.checkPlay(this, p, m)
    }

    companion object {
        @JvmStatic val C = CardConfig("Death", CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY)
        @JvmStatic val VARS = CardVariables().apply {
            cost(1)
            magic(40, 60)
            addModifier(ExhaustMod())
            addModifier(RequireStanceMod())
        }
    }
}