package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.thegrackle.helper.CardVariables

class CopyCrow : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        // TODO
    }

    companion object {
        @JvmStatic val C = CardConfig("CopyCrow", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF)
        @JvmStatic val VARS = CardVariables().apply {
            cost(1)
        }
    }
}