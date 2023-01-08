package io.bindernews.thegrackle;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import com.evacipated.cardcrawl.mod.stslib.icons.CustomIconHelper;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import io.bindernews.bnsts.EventEmit;
import io.bindernews.bnsts.Lazy;
import io.bindernews.thegrackle.api.IMultiHitManager;
import io.bindernews.thegrackle.cards.*;
import io.bindernews.thegrackle.icons.MusicNoteIcon;
import io.bindernews.thegrackle.patches.MetricsPatches;
import io.bindernews.thegrackle.power.BasePower;
import io.bindernews.thegrackle.relics.LoftwingFeather;
import io.bindernews.thegrackle.ui.CardClickableLink;
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

    /**
     * Map of miscellaneous UI strings
     */
    public static final Lazy<Map<String, String>> miscUI = Lazy.of(() ->
            CardCrawlGame.languagePack.getUIString("grackle:misc").TEXT_DICT);

    // Neat trick: fields in an interface are automatically 'public static final'
    // making it a convenient way to declare constants.
    /**
     * Various constants
     */
    public interface CO {
        /** Root path for image resources */
        String RES_IMAGES = MOD_RES + "/images";

        /** Root path of localization resources */
        String RES_LANG = MOD_RES + "/localization";

        String REG_START = "begin registering {}";

        String REG_END = "done registering {}";

        /** Metrics upload url */
        String METRICS_URL = "https://stats.grackle.bindernews.net";

        /** Sound effect ID */
        String SFX_QUACK = makeId("DUCK");
    }

    /**
     * Is the downfall mod installed?
     */
    public static boolean interopDownfall;

    /** Listeners for popup rendering. */
    public static final EventEmit<SpriteBatch> onPopupRender = new EventEmit<>();

    private static boolean hasInit = false;

    public GrackleMod() {
        BaseMod.subscribe(this);
        interopDownfall = Loader.isModLoaded("downfall");
        log.debug("registering color GRACKLE_BLACK");
        Grackle.Co.registerColor();
        log.debug("done registering colors");

        MetricsPatches.onMetricsRun.on(metrics -> {
            if (metrics.type == Metrics.MetricRequestType.UPLOAD_METRICS && Grackle.isPlaying()) {
                MetricsPatches.sendPost(metrics, CO.METRICS_URL);
            }
        });
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        if (!hasInit) {
            GrackleMod mod = new GrackleMod();
            ExtraHitsVariable.inst = new ExtraHitsVariable();
            hasInit = true;
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
        registerIcons();
        registerDynamicVariables();
        log.debug(CO.REG_START, "cards");
        AutoAdd aa = new AutoAdd(MOD_ID);
        aa.packageFilter(BaseCard.class);
        aa.cards();
        log.debug(CO.REG_END, "cards");
    }

    @Override
    public void receivePostInitialize() {
        registerPowers();
        val ignore1 = CardClickableLink.getInst();
    }


    private void registerPowers() {
        log.debug("registering powers");
        AutoAdd aa = new AutoAdd(MOD_ID);
        aa.packageFilter(BasePower.class);
        for (val c : MiscUtil.autoFindClasses(aa, AbstractPower.class)) {
            MiscUtil.registerPower(c);
        }
        log.debug("done registering powers");
    }

    private void registerDynamicVariables() {
        log.debug(CO.REG_START, "dynamic variables");
        BaseMod.addDynamicVariable(ExtraHitsVariable.inst);
        log.debug(CO.REG_END, "dynamic variables");
    }

    private void registerIcons() {
        log.debug(CO.REG_START, "icons");
        val list = new AbstractCustomIcon[] {
                MusicNoteIcon.getInst()
        };
        for (val icon : list) {
            CustomIconHelper.addCustomIcon(icon);
        }
        log.debug(CO.REG_END, "icons");
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
        BaseMod.addAudio(CO.SFX_QUACK, sfxPath + "duck_quack.ogg");
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
                new AerialAdvantage(),
                new BombingRun(),
                new BurnCream(),
                new Cackle(),
                new CrashLanding(),
                new Defend_GK(),
                new Duck(),
                new EvasiveManeuvers(),
                new FightFire(),
                new FireWithin(),
                new FiredUpCard(),
                new FireTouch(),
                new FireWithin(),
                new Flock(),
                new FOOF(),
                new Grenenade(),
                new HenPeck(),
                new InFlightService(),
                new Parachute(),
                new Paratrooper(),
                new PeckingOrder(),
                new PhoenixFeather(),
                new PhoenixForm(),
                new RocketGrackle(),
                new SelfBurn(),
                new BufferInputs(),
                new Strike_GK(),
                new SummonEgrets(),
                new Swoop(),
                new Takeoff(),
                new TargetingComputer(),
                new ThisWillHurt(),
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
                log.warn(e);
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

    public static IMultiHitManager getMultiHitManager() {
        return ExtraHitsVariable.inst;
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
