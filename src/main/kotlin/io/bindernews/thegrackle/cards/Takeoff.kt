package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.thegrackle.helper.CardVariables
import io.bindernews.thegrackle.helper.ModInterop
import io.bindernews.thegrackle.stance.StanceAloft

class Takeoff : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(GainBlockAction(p, p, block))
        addToBot(ModInterop.iop().changeStance(p, StanceAloft.STANCE_ID))
    }

    companion object {
        @JvmField
        val C = CardConfig("Takeoff", CardType.SKILL, CardRarity.BASIC, CardTarget.SELF)
        val VARS = CardVariables.config { c ->
            c.cost(1, -1)
            c.block(6, 10)
            c.tags(CardTags.STARTER_DEFEND)
        }
    }
}