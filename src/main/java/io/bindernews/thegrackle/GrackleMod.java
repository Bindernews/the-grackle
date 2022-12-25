package io.bindernews.thegrackle;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import io.bindernews.bnsts.Lazy;
import io.bindernews.thegrackle.cards.*;
import io.bindernews.thegrackle.helper.MultiHitManager;
import io.bindernews.thegrackle.icons.IconHelper;
import io.bindernews.thegrackle.relics.LoftwingFeather;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;


@SpireInitializer
public class GrackleMod implements
        AddAudioSubscriber, EditCharactersSubscriber, EditRelicsSubscriber, EditCardsSubscriber, EditStringsSubscriber,
        EditKeywordsSubscriber, PostInitializeSubscriber, OnStartBattleSubscriber
{
    public static final Logger log = LogManager.getLogger(GrackleMod.class);

    public static final String MOD_ID = "grackle";
    private static final String MOD_ID_COLON = MOD_ID + ":";
    public static final String MOD_RES = "grackleResources";

    /** Cache of loaded textures */
    private static final HashMap<String, Texture> textureCache = new HashMap<>();

    public interface CO {
        /** Root path for image resources */
        String RES_IMAGES = MOD_RES + "/images";

        /** Root path of localization resources */
        String RES_LANG = MOD_RES + "/localization";

        String REG_START = "begin registering {}";

        String REG_END = "done registering {}";
    }

    /**
     * Sound effect IDs
     */
    public interface Sfx {
        String QUACK = makeId("DUCK");
    }


    /**
     * Is the downfall mod installed?
     */
    public static boolean interopDownfall;

    public static MultiHitManager multiHitManager;

    private static boolean hasInit = false;

    public GrackleMod() {
        BaseMod.subscribe(this);
        interopDownfall = Loader.isModLoaded("downfall");
        log.debug("registering color GRACKLE_BLACK");
        Grackle.Co.registerColor();
        log.debug("done registering colors");
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        if (!hasInit) {
            GrackleMod mod = new GrackleMod();
            hasInit = true;
            multiHitManager = new MultiHitManager();
        }
    }

    @Override
    public void receiveEditCharacters() {
        log.debug("registering characters");
        Grackle.register();
        log.debug("done registering characters");
    }

    @Override
    public void receiveEditRelics() {
        // Add shared relics
//        Stream.of(
//                new PhoenixIdol()
//        ).forEach(r -> BaseMod.addRelic(r, RelicType.SHARED));

        // Add class-specific relics
        Stream.of(
                new LoftwingFeather()
        ).forEach(r -> BaseMod.addRelicToCustomPool(r, Grackle.En.COLOR_BLACK));
    }

    @Override
    public void receiveEditCards() {
        log.debug(CO.REG_START, "icons");
        IconHelper.registerAll();
        log.debug(CO.REG_END, "icons");
        log.debug(CO.REG_START, "dynamic variables");
        BaseMod.addDynamicVariable(new ExtraHitsVariable());
        log.debug(CO.REG_END, "dynamic variables");
        log.debug(CO.REG_START, "cards");
        AutoAdd aa = new AutoAdd(MOD_ID);
        aa.packageFilter(BaseCard.class);
        aa.cards();
        log.debug(CO.REG_END, "cards");
    }

    @Override
    public void receivePostInitialize() {
        registerPowers();
    }


    private void registerPowers() {
        log.debug("registering powers");
        AutoAdd aa = new AutoAdd(MOD_ID);
        aa.packageFilter(GrackleMod.class);
        for (val c : MiscUtil.autoFindClasses(aa, AbstractPower.class)) {
            MiscUtil.registerPower(c);
        }
        log.debug("done registering powers");
    }

    @Override
    public void receiveEditStrings() {
        MiscUtil.loadLocalization(CO.RES_LANG, Settings.GameLanguage.ENG);
        if (Settings.language != Settings.GameLanguage.ENG) {
            MiscUtil.loadLocalization(CO.RES_LANG, Settings.language);
        }
    }

    @Override
    public void receiveEditKeywords() {
        MiscUtil.loadKeywords(MOD_ID, CO.RES_LANG, Settings.GameLanguage.ENG);
        if (Settings.language != Settings.GameLanguage.ENG) {
            MiscUtil.loadKeywords(MOD_ID, CO.RES_LANG, Settings.language);
        }
    }

    @Override
    public void receiveAddAudio() {
        val sfxPath = MOD_RES + "/audio/";
        BaseMod.addAudio(Sfx.QUACK, sfxPath + "duck_quack.ogg");
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {

    }

    /**
     * Exists so that all cards are "used"
     */
    @SuppressWarnings("unused")
    private static ArrayList<AbstractCard> makeCardList() {
        val list = new ArrayList<AbstractCard>();
        Collections.addAll(list,
                new AerialAce(),
                new AttackR(),
                new BurnCream(),
                new Cackle(),
                new CrashLanding(),
                new Defend_GK(),
                new Duck(),
                new FightFire(),
                new FireWithin(),
                new FiredUpCard(),
                new FireTouch(),
                new FireWithin(),
                new Grenenade(),
                new HenPeck(),
                new PhoenixFeather(),
                new PhoenixForm(),
                new SelfBurn(),
                new BufferInputs(),
                new Strike_GK(),
                new SummonEgrets(),
                new Takeoff(),
                new TargetingComputer(),
                new TryThatAgain()
        );
        return list;
    }

    /**
     * Attempts to load and cache the texture at the given path.
     * @param path Path of the texture to load
     * @return Either the texture or {@code null} if not found
     */
    @Nullable
    public static Texture loadTexture(@NotNull String path) {
        return textureCache.computeIfAbsent(path, (path2) -> {
            try {
                val tex = new Texture(path2);
                tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                return tex;
            } catch (Exception e) {
                return null;
            }
        });
    }

    /**
     * Convenience function to lazy-load a texture atlas.
     * @param subPath Path relative to {@link CO#RES_IMAGES}
     * @return A lazy-loaded {@link TextureAtlas}
     */
    public static Lazy<TextureAtlas> lazyAtlas(String subPath) {
        return Lazy.of(() -> new TextureAtlas(CO.RES_IMAGES + subPath));
    }

    /**
     * Returns a new ID with the mod prefix.
     */
    public static String makeId(String name) {
        return MOD_ID_COLON + name;
    }

    /**
     * Returns a new ID with the mod prefix and the class name as the suffix.
     */
    public static String makeId(Class<?> clazz) {
        return makeId(clazz.getSimpleName());
    }


    /**
     * Remove the mod prefix and colon from {@code id}.
     * @param id ID string
     * @return ID with prefix removed
     */
    public static String removePrefix(String id) {
        if (id.startsWith(MOD_ID_COLON)) {
            return id.substring(MOD_ID_COLON.length());
        } else {
            return id;
        }
    }

}
