package io.bindernews.thegrackle;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtClass;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * A collection of miscellaneous utility functions that should be relatively re-usable for other
 * Slay the Spire mods. Feel free to copy-paste this file into your own projects, as long as you
 * retain the MIT license.
 *
 * @author bindernews
 */
public class MiscUtil {
    static final Logger log = LogManager.getLogger(MiscUtil.class.getPackage().getName());
    private static final Gson gson = new Gson();

    @Nullable @SneakyThrows
    public static <T> T nullCast(Class<T> clz, Object o) {
        if (clz.isInstance(o)) {
            return clz.cast(o);
        } else {
            return null;
        }
    }


    /**
     * Returns a new array list containing the given elements. This function exists in later versions
     * of Java, but not in Java 8. The original use-case was {@link CustomPlayer#getStartingDeck()}.
     */
    @SafeVarargs
    public static <E> ArrayList<E> arrayListOf(E ...elements) {
        ArrayList<E> lst = new ArrayList<>();
        lst.ensureCapacity(elements.length);
        Collections.addAll(lst, elements);
        return lst;
    }

    /**
     * Add an action to the bottom of the stack, convenience function.
     */
    public static void addToBot(AbstractGameAction a) {
        AbstractDungeon.actionManager.addToBottom(a);
    }

    /**
     * Add an action to the top of the stack, convenient static import.
     */
    public static void addToTop(AbstractGameAction a) {
        AbstractDungeon.actionManager.addToTop(a);
    }

    public static int getPowerAmount(AbstractCreature c, String powerId, int defaultAmount) {
        return Optional.ofNullable(c.getPower(powerId)).map(p -> p.amount).orElse(defaultAmount);
    }


    private static final HashMap<Class<?>, String> stringTypeMap = new HashMap<>();
    static {
        HashMap<Class<?>, String> m = stringTypeMap;
        m.put(BlightStrings.class, "blight");
        m.put(CardStrings.class, "card");
        m.put(CharacterStrings.class, "character");
        m.put(EventStrings.class, "event");
        m.put(MonsterStrings.class, "monster");
        m.put(OrbStrings.class, "orb");
        m.put(PotionStrings.class, "potion");
        m.put(PowerStrings.class, "power");
        m.put(RelicStrings.class, "relic");
        m.put(StanceStrings.class, "stance");
        m.put(UIStrings.class, "ui");
    }

    /**
     * Load all localization strings from the specified language at the path.
     *
     * <p>
     * The language directories must be lowercase, and files are named in the singular and without
     * the 'Strings' suffix. For example {@link CardStrings} would load {@code card.json}, and {@link UIStrings}
     * would load {@code ui.json}. Missing files will be ignored.
     * </p>
     *
     * @param path Resource directory containing languages, with no trailing slash (e.g. mymod/localization)
     * @param language Language code
     */
    public static void loadLocalization(String path, Settings.GameLanguage language) {
        String lang = language.name().toLowerCase();
        for (Class<?> st : stringTypeMap.keySet()) {
            String filePath = path + "/" + lang + "/" + stringTypeMap.get(st) + ".json";
            if (Gdx.files.internal(filePath).exists()) {
                BaseMod.loadCustomStringsFile(st, filePath);
                log.debug("loadLocalization: loaded " + filePath);
            }
        }
    }

    /**
     * Read a JSON file containing an array of Keyword entries
     * @param path The {@code localizations} directory, with no trailing slash
     * @param language Language code
     * @return Array of keywords
     */
    public static Keyword[] readKeywords(String path, Settings.GameLanguage language) {
        val filePath = langPath(path, "keyword.json", language);
        val content = Gdx.files.internal(filePath).readString(StandardCharsets.UTF_8.name());
        return gson.fromJson(content, Keyword[].class);
    }

    /**
     * Calls {@link MiscUtil#readKeywords} and then {@link BaseMod#addKeyword}.
     * @param modId The mod ID to prefix the keywords with
     * @param path The {@code localizations} directory, with no trailing slash
     * @param language Language code
     */
    public static void loadKeywords(String modId, String path, Settings.GameLanguage language) {
        val keywords = MiscUtil.readKeywords(path, language);
        for (val k : keywords) {
            BaseMod.addKeyword(modId, k.PROPER_NAME, k.NAMES, k.DESCRIPTION);
        }
    }

    public static String langPath(String base, String fileName, Settings.GameLanguage language) {
        val lang = language.name().toLowerCase();
        return base + "/" + lang + "/" + fileName;
    }

    public static void registerPower(Class<? extends AbstractPower> clazz) {
        String powerId = null;
        try {
            powerId = (String) clazz.getDeclaredField("POWER_ID").get(null);
        } catch (Exception ignored) {}
        if (powerId == null) {
            try {
                val ctor = clazz.getDeclaredConstructor(AbstractCreature.class, int.class);
                powerId = ctor.newInstance(null, 0).ID;
            } catch (Exception ignore) {}
        }
        if (powerId == null) {
            try {
                val ctor = clazz.getDeclaredConstructor();
                powerId = ctor.newInstance().ID;
            } catch (Exception ignore) {}
        }
        if (powerId == null) {
            throw new RuntimeException("cannot determine power ID for class " + clazz);
        }
        BaseMod.addPower(clazz, powerId);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T> List<Class<? extends T>> autoFindClasses(AutoAdd aa, Class<T> type) {
        ArrayList<Class<? extends T>> lst = new ArrayList<>();
        for (CtClass c : aa.findClasses(type)) {
            if (c.getAnnotation(AutoAdd.Ignore.class) != null) {
                continue;
            }
            Class<?> c2 = type.getClassLoader().loadClass(c.getName());
            lst.add((Class<? extends T>) c2);
        }
        return lst;
    }

}
