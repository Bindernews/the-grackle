package net.bindernews.grackle.helper

import com.megacrit.cardcrawl.core.CardCrawlGame
import net.bindernews.grackle.GrackleMod

open class DescriptionBuilder protected constructor() {
    val fragments: ArrayList<String> = ArrayList()
    var modifier: StringModifier = StringModifier.UPPERCASE_FIRST

    /**
     * When true, adding a fragment prepends a space.
     * Reset to `true` after every call to [add].
     */
    var insertSpace: Boolean = false

    fun atStartOfTurn(): DescriptionBuilder {
        return add(strings["start_of_turn"]!!)
    }

    fun gainPower(amount: String, power: String): DescriptionBuilder {
        return add(strings["gain_power"]!!.format(amount, power))
    }

    fun applyPower(amount: String, power: String): DescriptionBuilder {
        return add(strings["apply_power"]!!.format(amount, power))
    }

    fun dealDamage(amount: String): DescriptionBuilder {
        return add(strings["deal_damage"]!!.format(amount))
    }

    fun toAllEnemies(): DescriptionBuilder {
        return add(strings["to_all_enemies"]!!)
    }

    fun nTimes(amount: String): DescriptionBuilder {
        return add(strings["n_times"]!!.format(amount))
    }

    fun period(newLine: Boolean = true): DescriptionBuilder {
        insertSpace = false
        add(".")
        if (newLine) {
            newline()
        }
        modifier = StringModifier.UPPERCASE_FIRST
        return this
    }

    fun comma(): DescriptionBuilder {
        insertSpace = false
        return add(",")
    }

    fun tr(key: String): DescriptionBuilder {
        val msg = strings[key] ?: throw RuntimeException("unknown translation key '%s'".format(key))
        return add(msg)
    }

    fun newline(): DescriptionBuilder {
        val mod = modifier
        modifier = StringModifier.NONE
        add(strings["newline"]!!)
        modifier = mod
        return this
    }

    fun enterStance(stance: String): DescriptionBuilder {
        return add(strings["enter_stance"]!!.format(stance))
    }

    fun exitStance(): DescriptionBuilder {
        return add(strings["exit_stance"]!!)
    }

    fun withModifier(m: StringModifier): DescriptionBuilder {
        modifier = m
        return this
    }

    fun add(s: String): DescriptionBuilder {
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

enum class StringModifier {
    NONE,
    /**
     * Lowercase the first character of the string.
     */
    LOWERCASE_FIRST,
    /**
     * Uppercase the first character of the string.
     */
    UPPERCASE_FIRST;

    fun apply(s: String): String {
        return when (this) {
            NONE -> s
            LOWERCASE_FIRST -> {
                if (!s[0].isLowerCase()) {
                    s[0].lowercaseChar() + s.substring(1)
                } else {
                    s
                }
            }
            UPPERCASE_FIRST -> {
                if (!s[0].isUpperCase()) {
                    s[0].uppercaseChar() + s.substring(1)
                } else {
                    s
                }
            }
        }
    }
}
