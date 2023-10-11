package net.bindernews.grackle.helper

import basemod.AutoAdd
import basemod.BaseMod
import basemod.ReflectionHacks
import basemod.abstracts.CustomRelic
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.evacipated.cardcrawl.mod.stslib.Keyword
import com.google.gson.Gson
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.Settings.GameLanguage
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.localization.*
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.stances.AbstractStance
import net.bindernews.grackle.cards.BaseCard
import net.bindernews.grackle.cards.CardConfig
import net.bindernews.grackle.power.BasePower
import org.apache.logging.log4j.LogManager
import java.util.*

/**
 * A collection of miscellaneous utility functions that should be relatively re-usable for other Slay the Spire mods.
 * <br></br>
 * Feel free to copy-paste this file into your own projects, as long as you retain the MIT license.
 *
 * @author bindernews
 */
object MiscUtil {
    private val log = LogManager.getLogger(MiscUtil::class.java)!!
    private val gson = Gson()
    private val stringTypeMap = HashMap<Class<*>, String>()
    init {
        val m = stringTypeMap
        m[BlightStrings::class.java] = "blight"
        m[CardStrings::class.java] = "card"
        m[CharacterStrings::class.java] = "character"
        m[EventStrings::class.java] = "event"
        m[MonsterStrings::class.java] = "monster"
        m[OrbStrings::class.java] = "orb"
        m[PotionStrings::class.java] = "potion"
        m[PowerStrings::class.java] = "power"
        m[RelicStrings::class.java] = "relic"
        m[StanceStrings::class.java] = "stance"
        m[UIStrings::class.java] = "ui"
    }
    private const val KEYWORD_FILE = "keyword"

    /**
     * Add an action to the bottom of the stack, convenient static import.
     */
    fun addToBot(a: AbstractGameAction?) {
        AbstractDungeon.actionManager.addToBottom(a)
    }

    /**
     * Add an action to the top of the stack, convenient static import.
     */
    fun addToTop(a: AbstractGameAction?) {
        AbstractDungeon.actionManager.addToTop(a)
    }

    fun getPowerAmount(c: AbstractCreature, powerId: String, defaultAmount: Int): Int {
        return c.getPower(powerId)?.amount ?: defaultAmount
    }


    /**
     * Load all localization strings from the specified language at the path.
     *
     * The language directories must be lowercase, and files are named in the singular and without
     * the 'Strings' suffix. For example [CardStrings] would load `card.json`, and [UIStrings]
     * would load `ui.json`. Missing files will be ignored.
     *
     * @param path Resource directory containing languages, with no trailing slash (e.g. "mymod/localization")
     * @param languages Language codes
     */
    fun loadLocalization(path: String, vararg languages: GameLanguage) {
        val seen = HashSet<GameLanguage>()
        for (lang in languages) {
            // In case the same language is given multiple times
            if (!seen.add(lang)) {
                continue
            }
            val langStr = lang.toLowerStr()
            for (st in stringTypeMap.keys) {
                val fileRef = langFile(path, langStr, stringTypeMap[st]!!) ?: continue
                BaseMod.loadCustomStringsFile(st, fileRef.path())
                log.debug("loadLocalization: loaded ${fileRef.path()}")
            }
        }

    }

    /**
     * Reads keywords from 'keyword.json' and then calls [BaseMod.addKeyword] on each.
     * @param modId The mod ID to prefix the keywords with
     * @param path The `localizations` directory, with no trailing slash
     * @param languages Language codes
     */
    fun loadKeywords(modId: String?, path: String, vararg languages: GameLanguage) {
        val seen = HashSet<GameLanguage>()
        for (lang in languages) {
            // In case same language is given multiple times
            if (!seen.add(lang)) {
                continue
            }
            val langStr = lang.toLowerStr()
            val fileRef = langFile(path, langStr, KEYWORD_FILE) ?: continue
            val content = fileRef.readString("UTF-8")
            val keywords = gson.fromJson(content, Array<Keyword>::class.java)
            for (k in keywords) {
                BaseMod.addKeyword(modId, k.PROPER_NAME, k.NAMES, k.DESCRIPTION)
            }
            log.debug("loadKeywords: loaded ${fileRef.path()}")
        }
    }

    private fun GameLanguage.toLowerStr(): String = this.name.lowercase(Locale.getDefault())
    private fun langFile(base: String, langStr: String, fileName: String): FileHandle? {
        return Gdx.files.internal("$base/$langStr/${fileName}.json").takeIf { it.exists() }
    }

    fun getObjectId(clz: Class<*>): String {
        if (AbstractPower::class.java.isAssignableFrom(clz)) {
            @Suppress("UNCHECKED_CAST")
            val powerClazz = clz as Class<out AbstractPower>
            try {
                 return ReflectionHacks.getPrivateStatic(clz, "POWER_ID")
            } catch (_: Exception) {}
            try {
                val ctor = powerClazz.getDeclaredConstructor(AbstractCreature::class.java, Int::class.javaPrimitiveType)
                return ctor.newInstance(null, 0).ID
            } catch (_: Exception) { }
            try {
                val ctor = powerClazz.getDeclaredConstructor()
                return ctor.newInstance().ID
            } catch (_: Exception) { }
        } else if (BaseCard::class.java.isAssignableFrom(clz)) {
            try {
                return ReflectionHacks.getPrivateStatic<CardConfig>(clz, "C").ID
            } catch (_: Exception) {}
        } else if (CustomRelic::class.java.isAssignableFrom(clz)) {
            try {
                return ReflectionHacks.getPrivateStatic(clz, "ID")
            } catch (_: Exception) {}
        } else if (AbstractStance::class.java.isAssignableFrom(clz)) {
            try {
                return ReflectionHacks.getPrivateStatic(clz, "STANCE_ID")
            } catch (_: Exception) {}
        }
        // Fallback returns empty string
        return ""
    }

    fun <T> autoFindClasses(aa: AutoAdd, type: Class<T>): List<Class<out T>> {
        val lst = ArrayList<Class<out T>>()
        for (c in aa.findClasses(type)) {
            if (c.getAnnotation(AutoAdd.Ignore::class.java) != null) {
                continue
            }
            val c2 = type.classLoader.loadClass(c.name)
            @Suppress("UNCHECKED_CAST")
            lst.add(c2 as Class<out T>)
        }
        return lst
    }
}