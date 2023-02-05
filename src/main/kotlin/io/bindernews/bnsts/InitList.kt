package io.bindernews.bnsts

import com.megacrit.cardcrawl.cards.AbstractCard
import java.util.function.Consumer

class InitList : ICardInitializer {
    private val doInit = ArrayList<Consumer<AbstractCard>>()
    private val doUpgrade = ArrayList<Consumer<AbstractCard>>()
    fun onInit(action: Consumer<AbstractCard>) {
        doInit.add(action)
    }

    fun onUpgrade(action: Consumer<AbstractCard>) {
        doUpgrade.add(action)
    }

    override fun init(card: AbstractCard) {
        for (c in doInit) {
            c.accept(card)
        }
    }

    override fun forceUpgrade(card: AbstractCard) {
        for (c in doUpgrade) {
            c.accept(card)
        }
    }
}