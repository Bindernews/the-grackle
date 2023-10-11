package net.bindernews.grackle.helper

import basemod.BaseMod
import basemod.ReflectionHacks
import com.google.gson.stream.JsonWriter
import com.megacrit.cardcrawl.core.Settings.GameLanguage
import com.megacrit.cardcrawl.localization.CardStrings
import com.megacrit.cardcrawl.localization.PowerStrings
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.cards.BaseCard
import net.bindernews.grackle.power.BasePower
import java.io.File
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType

class StringSaver(val lang: GameLanguage) {

    fun exportStrings(destination: File) {
        val exportMsg = "exporting {} strings"

        GrackleMod.log.info(exportMsg, "card")
        val cardStrings = HashMap<String, CardStrings>()
        for (c in GrackleMod.autoFindClasses(BaseCard::class.java, null)) {
            saveStrings(c, cardStrings, CardStrings::class.java)
        }
        saveJson(cardStrings, File(destination, "card.json"))

        GrackleMod.log.info(exportMsg, "power")
        val powerStrings = HashMap<String, PowerStrings>()
        for (c in GrackleMod.autoFindClasses(BasePower::class.java, null)) {
            saveStrings(c, powerStrings, PowerStrings::class.java)
        }
        saveJson(powerStrings, File(destination, "power.json"))
    }

    private fun saveJson(data: Any, dest: File) {
        val jsonData = BaseMod.gson.toJsonTree(data)
        val wr = JsonWriter(dest.bufferedWriter())
        wr.setIndent("  ")
        wr.use { BaseMod.gson.toJson(jsonData, it) }
    }

    /**
     * Helper function to get the companion object and call `saveStrings` on it.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T> saveStrings(clz: Class<*>, dest: MutableMap<String, T>, stringsClz: Class<T>): Boolean {
        val mt = MethodType.methodType(stringsClz, GameLanguage::class.java)
        val companionObj = ReflectionHacks.getPrivateStatic<Any>(clz, "Companion")
        return try {
            val handle = MethodHandles.lookup().findVirtual(companionObj.javaClass, "saveStrings", mt)
            val stringsObj = handle.invoke(companionObj, lang) as T
            // Determine the thing's id
            val thingId = MiscUtil.getObjectId(clz)
            if (thingId.isEmpty()) {
                return false
            }
            dest[thingId] = stringsObj
            true
        } catch (e: NoSuchMethodException) {
            false
        }
    }
}

interface ISaveStrings<T> {
    fun saveStrings(lang: GameLanguage): T
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