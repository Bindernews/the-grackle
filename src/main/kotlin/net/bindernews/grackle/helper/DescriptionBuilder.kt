package net.bindernews.grackle.helper

import com.megacrit.cardcrawl.core.CardCrawlGame
import net.bindernews.grackle.GrackleMod

open class DescriptionBuilder protected constructor() {
    val fragments: ArrayList<String> = ArrayList()
    var modifier: StringModifier = StringModifier.UPPERCASE_FIRST

    /**
     * When true, adding a fragment prepends a space.
     * Reset to `true` after every call to [addFragment].
     */
    var insertSpace: Boolean = false

    fun atStartOfTurn(): DescriptionBuilder {
        return addFragment(strings["start_of_turn"]!!)
    }

    fun applyPower(amount: String, power: String): DescriptionBuilder {
        return addFragment(strings["apply_power"]!!.format(amount, power))
    }

    fun dealDamage(amount: String): DescriptionBuilder {
        return addFragment(strings["deal_damage"]!!.format(amount))
    }

    fun toAllEnemies(): DescriptionBuilder {
        return addFragment(strings["to_all_enemies"]!!)
    }

    fun nTimes(amount: String): DescriptionBuilder {
        return addFragment(strings["n_times"]!!.format(amount))
    }

    fun period(newLine: Boolean = true): DescriptionBuilder {
        insertSpace = false
        addFragment(".")
        if (newLine) {
            newline()
        }
        modifier = StringModifier.UPPERCASE_FIRST
        return this
    }

    fun comma(): DescriptionBuilder {
        insertSpace = false
        return addFragment(",")
    }

    fun innate(): DescriptionBuilder {
        return addFragment(strings["innate"]!!)
    }

    fun exhaust(): DescriptionBuilder {
        return addFragment(strings["exhaust"]!!)
    }

    fun newline(): DescriptionBuilder {
        val mod = modifier
        modifier = StringModifier.NONE
        addFragment(strings["newline"]!!)
        modifier = mod
        return this
    }

    fun enterStance(stance: String): DescriptionBuilder {
        return addFragment(strings["enter_stance"]!!.format(stance))
    }

    fun exitStance(): DescriptionBuilder {
        return addFragment(strings["exit_stance"]!!)
    }

    fun withModifier(m: StringModifier): DescriptionBuilder {
        modifier = m
        return this
    }

    fun addFragment(s: String): DescriptionBuilder {
        // If user is explicitly adding spaces, don't do it automatically.
        if (s.startsWith(" ")) {
            insertSpace = false
        }
        val prefix = if (insertSpace) " " else ""
        fragments.add(prefix + modifier.apply(s))
        modifier = StringModifier.NONE
        insertSpace = true
        return this
    }

    fun build(): String = fragments.joinToString("")

    companion object {
        val strings: Map<String, String> by lazy {
            CardCrawlGame.languagePack.getUIString("${GrackleMod.MOD_ID}:DescriptionBuilder").TEXT_DICT
        }

        /**
         * Returns a new description builder appropriate for the current language.
         * This will allow sub-classing in case we ever support RtL languages.
         */
        fun create(): DescriptionBuilder {
            return DescriptionBuilder()
        }
    }

}