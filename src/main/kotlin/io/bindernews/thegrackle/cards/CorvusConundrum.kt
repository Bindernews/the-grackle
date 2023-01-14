package io.bindernews.thegrackle.cards

import io.bindernews.bnsts.CardVariables

class CorvusConundrum : BaseCard(C, VARS) {

    companion object {
        @JvmStatic val C = CardConfig("CorvusConundrum", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        @JvmStatic val VARS = CardVariables().apply {
            cost(1)
        }
    }
}