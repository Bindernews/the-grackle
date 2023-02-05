package io.bindernews.bnsts

import com.megacrit.cardcrawl.cards.AbstractCard

interface ICardInitializer {
    fun init(card: AbstractCard)
    fun upgrade(card: AbstractCard) {
        if (!card.upgraded) {
            forceUpgrade(card)
        }
    }
    fun forceUpgrade(card: AbstractCard)
}