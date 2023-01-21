package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.bnsts.CardVariables

class CopyCrow : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        // TODO
    }

    companion object {
        @JvmStatic val C = CardConfig("CorvusConundrum", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF)
        @JvmStatic val VARS = CardVariables().apply {
            cost(1)
        }
    }
}