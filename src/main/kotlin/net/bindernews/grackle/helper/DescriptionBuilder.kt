package net.bindernews.grackle.helper

import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import net.bindernews.grackle.GrackleMod
import java.util.*

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

        private val dollarDelimited = """\$([^$]*)\$""".toRegex()
        private val parenRegex = """\(([^()$]+)\)""".toRegex()
        private val gameLocale by lazy {
            when (Settings.language) {
                Settings.GameLanguage.ENG -> Locale.ENGLISH
                Settings.GameLanguage.FIN -> Locale("fi", "FI")
                else -> Locale.getDefault()
            }
        }

        /**
         * Returns a new description builder appropriate for the current language.
         * This will allow sub-classing in case we ever support RtL languages.
         */
        fun create(): DescriptionBuilder {
            return DescriptionBuilder()
        }


        fun parse(source: String): String {
            val sb = StringBuilder()
            val segments = splitByRegex(dollarDelimited, source, 1)
            for (i in segments.indices) {
                val segment = segments[i]
                if (i % 2 == 0) {
                    // add non-match
                    sb.append(segment)
                } else if (segment == "") {
                    // empty match means insert a literal $
                    sb.append("$");
                } else if (segment == "\n") {
                    // Insert newline
                    sb.append(" NL ")
                } else {
                    val partArgs = segment.split(",")
                    val trKey = partArgs[0]
                    // Convert case and append matched string
                    val shouldUpper = trKey[0].isUpperCase()
                    val matchLower = trKey.lowercase(gameLocale)
                    var convert = (strings[matchLower] ?: matchLower)
                    if (partArgs.size > 1) {
                        convert = String.format(convert, *partArgs.drop(1).toTypedArray())
                    }
                    if (shouldUpper) {
                        convert = uppercaseFirstChar(convert)
                    }
                    sb.append(convert)
                }
            }
            return sb.toString()
        }

        private fun uppercaseFirstChar(s: String): String {
            return s[0].uppercaseChar() + s.substring(1)
        }

        fun splitByRegex(regex: Regex, input: String, group: Int = 0): ArrayList<String> {
            val parts = arrayListOf<String>()
            var index = 0
            while (true) {
                val m = regex.find(input, index)
                if (m != null) {
                    // Add pre-match and the match, then update index
                    parts.add(input.substring(index, m.range.first))
                    parts.add(m.groupValues[group])
                    index = m.range.last + 1
                } else {
                    // Add remainder of string after match
                    parts.add(input.substring(index))
                    return parts
                }
            }
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
