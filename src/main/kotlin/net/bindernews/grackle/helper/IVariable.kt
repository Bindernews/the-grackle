package net.bindernews.grackle.helper

import com.megacrit.cardcrawl.cards.AbstractCard

interface IVariable {
    fun value(card: AbstractCard): Int
    fun baseValue(card: AbstractCard): Int
    fun upgraded(card: AbstractCard): Boolean
    fun isModified(card: AbstractCard): Boolean
    fun setValue(card: AbstractCard, amount: Int)
    fun setBaseValue(card: AbstractCard, amount: Int)
    fun upgrade(card: AbstractCard, amount: Int)
}