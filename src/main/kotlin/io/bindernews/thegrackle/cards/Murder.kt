package io.bindernews.thegrackle.cards

import io.bindernews.bnsts.CardVariables

class Murder : BaseCard(C, VARS) {

    companion object {
        @JvmStatic val C = CardConfig("Murder", CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY)
        @JvmStatic val VARS = CardVariables().apply {
            cost(1)
            damage(20, 30)
            onInit {
                it.isInnate = true
                it.exhaust = true
            }
        }
    }
}