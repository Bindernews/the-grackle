package io.bindernews.thegrackle.cards

import basemod.AutoAdd.Seen
import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.thegrackle.helper.CardVariables

@Seen
class Defend_GK : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(GainBlockAction(p, p, block))
    }

    companion object {
        @JvmField
        val C = CardConfig("Defend_GK", CardType.SKILL, CardRarity.BASIC, CardTarget.SELF)
        val VARS = CardVariables.config { c ->
            c.cost(1, -1)
            c.block(5, 8)
            c.tags(CardTags.STARTER_DEFEND)
        }
    }
}