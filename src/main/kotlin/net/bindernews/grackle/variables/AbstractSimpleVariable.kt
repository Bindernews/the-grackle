package net.bindernews.grackle.variables

import basemod.abstracts.DynamicVariable
import com.evacipated.cardcrawl.modthespire.lib.SpireField
import com.megacrit.cardcrawl.cards.AbstractCard
import net.bindernews.grackle.helper.IVariable

/**
 * This class splits the raw manipulation of a dynamic variable from its common
 * usage (e.g. [ExtraHitsVariable]), and reduces overall memory usage
 * by adding only one field to [AbstractCard] instead of three, and
 * only allocating instances as necessary.
 * <br></br>
 * The field value should default to `null`. When one of the `set*`
 * methods of [AbstractSimpleVariable] is called, the internal field will be initialized.
 */
abstract class AbstractSimpleVariable(
    /** The field instance that we're storing values in. */
    @JvmField protected val field: SpireField<VariableInst?>,
    @JvmField val defaultValue: VariableInst,
) : DynamicVariable(), IVariable {
    override fun isModified(card: AbstractCard): Boolean {
        return value(card) != baseValue(card)
    }

    override fun value(card: AbstractCard): Int {
        val f = field[card]
        return f?.value ?: defaultValue.value
    }

    override fun baseValue(card: AbstractCard): Int {
        val f = field[card]
        return f?.baseValue ?: defaultValue.baseValue
    }

    override fun upgraded(card: AbstractCard): Boolean {
        val f = field[card]
        return f?.upgraded ?: defaultValue.upgraded
    }

    override fun setBaseValue(card: AbstractCard, amount: Int) {
        val f = initField(card)
        f.value = amount
        f.baseValue = amount
        card.initializeDescription()
    }

    override fun setValue(card: AbstractCard, amount: Int) {
        initField(card).value = amount
    }

    override fun upgrade(card: AbstractCard, amount: Int) {
        val f = initField(card)
        f.upgraded = true
        f.baseValue += amount
        f.value += amount
    }

    /**
     * Returns the [VariableInst] field of the card, initializing it if it's null.
     */
    private fun initField(card: AbstractCard): VariableInst {
        var f = field[card]
        if (f == null) {
            f = VariableInst(defaultValue.value, defaultValue.baseValue, defaultValue.upgraded)
            field[card] = f
        }
        return f
    }

    /**
     * The backing field type that stores field data.
     */
    data class VariableInst(var value: Int = -1, var baseValue: Int = -1, var upgraded: Boolean = false)
}