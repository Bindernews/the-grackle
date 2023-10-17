package net.bindernews.grackle.variables

import basemod.abstracts.DynamicVariable
import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.Settings
import net.bindernews.grackle.GrackleMod.Companion.MOD_ID
import net.bindernews.grackle.helper.IVariable

class SpeedBoostVar : DynamicVariable(), IVariable {
    override fun key(): String = KEY
    override fun isModified(card: AbstractCard): Boolean {
        return if (card is SpeedBoostCard) {
            card.speedBoost.value != card.speedBoost.baseValue
        } else {
            false
        }
    }

    override fun value(card: AbstractCard): Int {
        return if (card is SpeedBoostCard) card.speedBoost.value else -1
    }
    override fun baseValue(card: AbstractCard): Int {
        return if (card is SpeedBoostCard) card.speedBoost.baseValue else -1
    }

    override fun upgraded(card: AbstractCard): Boolean {
        return if (card is SpeedBoostCard) card.speedBoost.upgraded else false
    }

    override fun setValue(card: AbstractCard, amount: Int) {
        if (card is SpeedBoostCard) card.speedBoost.value = amount
    }

    override fun setBaseValue(card: AbstractCard, amount: Int) {
        if (card is SpeedBoostCard) card.speedBoost.baseValue = amount
    }

    override fun upgrade(card: AbstractCard, amount: Int) {
        if (card is SpeedBoostCard) {
            card.speedBoost.value += amount
            card.speedBoost.baseValue += amount
            card.speedBoost.upgraded = true
        }
    }

    override fun getIncreasedValueColor(): Color = Settings.RED_TEXT_COLOR
    override fun getDecreasedValueColor(): Color = Settings.GREEN_TEXT_COLOR

    companion object {
        const val KEY = "$MOD_ID:speedBoost"
        @JvmField val inst = SpeedBoostVar()
    }
}