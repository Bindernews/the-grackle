package net.bindernews.grackle.helper

import basemod.abstracts.AbstractCardModifier
import basemod.abstracts.DynamicVariable
import basemod.helpers.CardModifierManager
import basemod.helpers.dynamicvariables.BlockVariable
import basemod.helpers.dynamicvariables.DamageVariable
import basemod.helpers.dynamicvariables.MagicNumberVariable
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.ExhaustiveField
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.PersistFields
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.RefundFields
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable
import com.evacipated.cardcrawl.mod.stslib.variables.PersistVariable
import com.evacipated.cardcrawl.mod.stslib.variables.RefundVariable
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import lombok.SneakyThrows
import net.bindernews.bnsts.IField
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.util.*
import java.util.function.Consumer

/**
 * A utility for easily configuring an [AbstractCard]'s values.
 * <br></br>
 * Calling [CardVariables.config] allows users to conveniently create and
 * set values. StSLib's [DynamicVariable]s are supported, as well as anything
 * implementing [IVariable], allowing user expansion.
 *
 * @author bindernews
 */
class CardVariables : ICardInitializer {
    private val settings = ArrayList<VariableSetting>()
    private val children = ArrayList<ICardInitializer>()
    private val initList = ArrayList<Consumer<AbstractCard>>()
    private val upgradeList = ArrayList<Consumer<AbstractCard>>()

    fun add(variable: IVariable, value: Int, valueUpg: Int) {
        settings.add(VariableSetting(variable, value, valueUpg))
    }

    @Suppress("unused")
    fun child(initializer: ICardInitializer) {
        children.add(initializer)
    }

    fun addModifier(modifier: AbstractCardModifier) {
        onInit { CardModifierManager.addModifier(it, modifier) }
    }

    fun block(base: Int, upg: Int = -1) { add(vBlock, base, upg) }
    fun cost(base: Int, upg: Int = -1) { add(vCost, base, upg) }
    fun damage(base: Int, upg: Int = -1) { add(vDamage, base, upg) }
    fun magic(base: Int, upg: Int = -1) { add(vMagic, base, upg) }

    fun multiDamage(base: Boolean, upg: Boolean) {
        initList.add { fIsMultiDamage[it] = base }
        upgradeList.add { fIsMultiDamage[it] = upg }
    }

    fun tags(vararg tags: AbstractCard.CardTags) {
        onInit { card -> Collections.addAll(card.tags, *tags) }
    }

    fun onInit(action: Consumer<AbstractCard>) {
        initList.add(action)
    }

    fun onUpgrade(action: Consumer<AbstractCard>) {
        upgradeList.add(action)
    }

    override fun init(card: AbstractCard) {
        for (s in settings) {
            s.variable.setBaseValue(card, s.value)
            s.variable.setValue(card, s.value)
        }
        children.forEach(Consumer { a: ICardInitializer -> a.init(card) })
        initList.forEach { it.accept(card) }
        card.initializeDescription()
    }

    override fun upgrade(card: AbstractCard) {
        if (!card.upgraded) {
            upgradeName(card)
            forceUpgrade(card)
        }
    }

    override fun forceUpgrade(card: AbstractCard) {
        for (s in settings) {
            if (s.valueUpg != -1) {
                s.variable.upgrade(card, s.valueUpg - s.value)
            }
        }
        children.forEach { it.forceUpgrade(card) }
        upgradeList.forEach { it.accept(card) }
    }

    internal data class VariableSetting(val variable: IVariable, val value: Int = 0, val valueUpg: Int = 0)

    fun interface AcceptCard {
        @Throws(Throwable::class)
        fun accept(card: AbstractCard, amount: Int)
    }

    companion object {
        @JvmStatic fun config(c: Consumer<CardVariables>): CardVariables {
            val cv = CardVariables()
            c.accept(cv)
            return cv
        }

        private val hUpgradeDamage: MethodHandle
        private val hUpgradeBlock: MethodHandle
        private val hUpgradeMagic: MethodHandle
        private val hUpgradeName: MethodHandle
        private val hUpgradeCost: MethodHandle
        val fIsMultiDamage = IField.unreflect(AbstractCard::class.java, Boolean::class.java, "isMultiDamage")
        val fDamageType = IField.unreflect(AbstractCard::class.java, DamageType::class.java, "damageType")

        init {
            val mtypeInt = MethodType.methodType(Void.TYPE, Int::class.javaPrimitiveType)
            val mtypeNone = MethodType.methodType(Void.TYPE)
            hUpgradeDamage = findCardMethod("upgradeDamage", mtypeInt)
            hUpgradeBlock = findCardMethod("upgradeBlock", mtypeInt)
            hUpgradeMagic = findCardMethod("upgradeMagicNumber", mtypeInt)
            hUpgradeCost = findCardMethod("upgradeBaseCost", mtypeInt)
            hUpgradeName = findCardMethod("upgradeName", mtypeNone)
        }

        private fun findCardMethod(name: String, args: MethodType): MethodHandle {
            return try {
                val m = AbstractCard::class.java.getDeclaredMethod(name, *args.parameterArray())
                m.isAccessible = true
                MethodHandles.lookup().unreflect(m)
            } catch (ex: Exception) {
                throw RuntimeException(ex)
            }
        }

        fun upgradeName(card: AbstractCard) {
            hUpgradeName.invoke(card)
        }

        val vCost: IVariable = object :
            IVariable {
            override fun value(card: AbstractCard): Int {
                return card.costForTurn
            }

            override fun baseValue(card: AbstractCard): Int {
                return card.cost
            }

            override fun upgraded(card: AbstractCard): Boolean {
                return card.upgradedCost
            }

            override fun isModified(card: AbstractCard): Boolean {
                return card.isCostModified
            }

            override fun setValue(card: AbstractCard, amount: Int) {
                card.costForTurn = amount
            }

            override fun setBaseValue(card: AbstractCard, amount: Int) {
                card.cost = amount
            }
            override fun upgrade(card: AbstractCard, amount: Int) {
                hUpgradeCost.invoke(card, baseValue(card) + amount)
            }
        }
        val vBlock = buildForDynamicVariable(
            BlockVariable(),
            AbstractCard::block::set,
            AbstractCard::baseBlock::set,
            hUpgradeBlock::invoke)
        val vDamage = buildForDynamicVariable(
            DamageVariable(),
            AbstractCard::damage::set,
            AbstractCard::baseDamage::set,
            hUpgradeDamage::invoke)
        val vMagic = buildForDynamicVariable(
            MagicNumberVariable(),
            AbstractCard::magicNumber::set,
            AbstractCard::baseMagicNumber::set,
            hUpgradeMagic::invoke)
        val vExhaustive = buildForDynamicVariable(
            ExhaustiveVariable(),
            ExhaustiveField.ExhaustiveFields.exhaustive::set,
            ExhaustiveVariable::setBaseValue,
            ExhaustiveVariable::upgrade)
        val vRefund = buildForDynamicVariable(
            RefundVariable(),
            RefundFields.refund::set,
            RefundVariable::setBaseValue,
            RefundVariable::upgrade)
        val vPersist = buildForDynamicVariable(
            PersistVariable(),
            PersistFields.persist::set,
            PersistFields::setBaseValue,
            PersistFields::upgrade)

        fun buildForDynamicVariable(
            dynVar: DynamicVariable,
            fSetValue: AcceptCard,
            fSetBaseValue: AcceptCard,
            fUpgrade: AcceptCard
        ): IVariable {
            return object : IVariable {
                override fun value(card: AbstractCard): Int {
                    return dynVar.value(card)
                }

                override fun baseValue(card: AbstractCard): Int {
                    return dynVar.baseValue(card)
                }

                override fun upgraded(card: AbstractCard): Boolean {
                    return dynVar.upgraded(card)
                }

                override fun isModified(card: AbstractCard): Boolean {
                    return dynVar.isModified(card)
                }

                @SneakyThrows
                override fun setValue(card: AbstractCard, amount: Int) {
                    fSetValue.accept(card, amount)
                }

                @SneakyThrows
                override fun setBaseValue(card: AbstractCard, amount: Int) {
                    fSetBaseValue.accept(card, amount)
                }

                @SneakyThrows
                override fun upgrade(card: AbstractCard, amount: Int) {
                    fUpgrade.accept(card, amount)
                }
            }
        }
    }
}