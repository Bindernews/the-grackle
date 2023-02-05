package io.bindernews.thegrackle.cards

import basemod.cardmods.ExhaustMod
import com.megacrit.cardcrawl.actions.common.DrawCardAction
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.helper.ModInterop.Companion.iop
import io.bindernews.thegrackle.helper.magic2
import io.bindernews.thegrackle.stance.StanceAloft

class AerialAdvantage : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        // N.B Not applicable for charboss
        addToBot(iop().changeStance(p, StanceAloft.STANCE_ID))
        addToBot(iop().actionGainEnergy(p, magicNumber))
        addToBot(DrawCardAction(p, magic2))
    }

    companion object {
        val C = CardConfig("AerialAdvantage", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        val VARS = CardVariables.config { c ->
            c.cost(0)
            c.magic(2, 3)
            c.magic2(4, -1)
            c.addModifier(ExhaustMod())
        }
    }
}