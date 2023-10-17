package net.bindernews.grackle.helper

import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.core.Settings.GameLanguage
import com.megacrit.cardcrawl.helpers.GameDictionary
import com.megacrit.cardcrawl.localization.LocalizedStrings
import net.bindernews.grackle.GrackleMod
import org.apache.commons.lang3.StringUtils
import java.lang.annotation.RetentionPolicy
import java.util.*
import java.util.function.Function
import kotlin.collections.HashMap

typealias BuildFn = DescriptionBuilder.(Boolean) -> String

open class DescriptionBuilder protected constructor(protected val buildFn: BuildFn) : Function<Boolean, String> {

    /**
     * The language to use when localizing. Default is English.
     */
    var lang: GameLanguage = GameLanguage.ENG
        set(value) {
            // Clear cache
            Arrays.fill(cache, null)
            field = value
        }

    /**
     * Extra translated strings, usually from EXTENDED_DESCRIPTION.
     * May be accessed using `text0`, `text1`, etc. as keys in [format].
     */
    var text: Array<String> = emptyArray()

    /**
     * String cache
     */
    private val cache = arrayOf<String?>(null, null)

    /**
     * Returns the upgraded or non-upgraded description string.
     * This value may be cached.
     */
    fun get(upgraded: Boolean): String {
        return if (upgraded) {
            arrayCompute(cache, 1) { build(true) }
        } else {
            arrayCompute(cache, 0) { build(false) }
        }
    }

    override fun apply(t: Boolean): String = get(t)

    fun build(upgraded: Boolean): String {
        return buildFn.invoke(this, upgraded)
    }

    /**
     * Returns a string representing gaining the given amount of energy.
     * @param amount amount of energy gained
     */
    fun energyStr(amount: Int): String {
        return " [E]".repeat(amount)
    }

    /**
     * Format the string using `{<specifier>}` for replacements. If the specifier is a string, it will be
     * looked up in [Companion.strings]. If it's a number _N_, it will be replaced with parameter _N_.
     *
     * @param fmt format string
     * @param params Arguments to format string
     */
    fun format(fmt: String, vararg params: Any): String {
        return fmt.replace(formatRegex) { match ->
            val modifiers = match.groupValues[1]
            val key = match.groupValues[2]
            // If the key is an int, lookup in params array, otherwise lookup in strings
            var outStr = key.toIntOrNull()?.let { params[it].toString() }
                ?: strings[key]
                ?: lookupText(key)
                ?: throw RuntimeException("invalid format key '%s'".format(key))
            // Apply modifiers
            for (c in modifiers) {
                val mod = when (c) {
                    '+' -> StringModifier.UPPERCASE_FIRST
                    '-' -> StringModifier.LOWERCASE_FIRST
                    else -> StringModifier.NONE
                }
                outStr = mod.apply(outStr)
            }
            outStr
        }
    }

    private fun lookupText(key: String): String? {
        if (key.startsWith("text")) {
            val index = key.substring(4).toIntOrNull() ?: return null
            return text[index]
        } else {
            return null
        }
    }

    companion object {
        /**
         * Translation string map. Merges translations from various sources for formatting.
         * Hopefully this will allow cards to be easily translated.
         */
        val strings: Map<String, String> by lazy {
            val m = HashMap<String, String>()
            // Add game dictionary values
            listOf(
                "Artifact" to GameDictionary.ARTIFACT,
                "Block" to GameDictionary.BLOCK,
                "Dexterity" to GameDictionary.DEXTERITY,
                "Ethereal" to GameDictionary.ETHEREAL,
                "Exhaust" to GameDictionary.EXHAUST,
                "Innate" to GameDictionary.INNATE,
                "Intangible" to GameDictionary.INTANGIBLE,
                "Retain" to GameDictionary.RETAIN,
                "Scry" to GameDictionary.SCRY,
                "Strength" to GameDictionary.STRENGTH,
                "Thorns" to GameDictionary.THORNS,
                "Vigor" to GameDictionary.VIGOR,
            ).forEach {
                m[it.first] = StringUtils.capitalize(it.second.NAMES[0])
            }
            // Try to extract some words from card strings
            m.putAll(extractCardStrings())
            // Add my own localization stuff, also overriding any language-specific values that
            // were incorrectly extracted from cards.
            m.putAll(CardCrawlGame.languagePack.getUIString("${GrackleMod.MOD_ID}:DescriptionBuilder").TEXT_DICT)
            m
        }

        /**
         * Tries to extract various key-words and phrases from card descriptions.
         */
        private fun extractCardStrings(): Map<String, String> {
            // NOTE: Need different mechanism for LtR languages

            val m = HashMap<String, String>()
            // Extract "deal", "damage", "to ALL enemies" from "A Thousand Cuts" card text
            val atcDesc = CardCrawlGame.languagePack.getCardStrings("A Thousand Cuts").DESCRIPTION.split(" ")
            atcDesc.indexOf("!M!").let {
                m["deal"] = atcDesc[it - 1]
                m["Deal"] = StringUtils.capitalize(m["deal"])
                m["damage"] = atcDesc[it + 1]
                m["to_all_enemies"] = atcDesc.slice(it + 2 until atcDesc.size).joinToString(" ")
            }
            // Extract "Gain" and "Draw" from "Adrenaline"
            val adrenalineDesc = CardCrawlGame.languagePack.getCardStrings("Adrenaline").DESCRIPTION.split(" ")
            m["Gain"] = adrenalineDesc[0]
            m["Draw"] = adrenalineDesc[adrenalineDesc.indexOf("2") - 1]
            // Extract "discard" from "Acrobatics"
            return m
        }

        private val formatRegex = Regex("""(?<!\\)\{([+-]*)(\w+)}""")

        @JvmStatic fun create(text: Array<String>, buildFn: BuildFn): DescriptionBuilder {
            val b = DescriptionBuilder(buildFn)
            b.text = text
            return b
        }

        /**
         * Returns a new description builder appropriate for the current language.
         * This will allow sub-classing in case we ever support RtL languages.
         */
        @JvmStatic fun create(buildFn: BuildFn): DescriptionBuilder {
            return DescriptionBuilder(buildFn)
        }

        @JvmStatic fun <T> arrayCompute(ar: Array<T?>, index: Int, compute: (Int) -> T): T {
            if (ar[index] == null) {
                ar[index] = compute(index)
            }
            return ar[index]!!
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