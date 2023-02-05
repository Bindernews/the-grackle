package io.bindernews.thegrackle;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import io.bindernews.bnsts.Lazy;
import io.bindernews.bnsts.MiscUtil;
import io.bindernews.thegrackle.api.IMultiHitManager;
import io.bindernews.thegrackle.cards.*;
import io.bindernews.thegrackle.helper.ExtKt;
import io.bindernews.thegrackle.icons.IconsKt;
import io.bindernews.thegrackle.power.BasePower;
import io.bindernews.thegrackle.relics.BerserkerTotem;
import io.bindernews.thegrackle.relics.LoftwingFeather;
import io.bindernews.thegrackle.relics.SimmeringHeat;
import io.bindernews.thegrackle.ui.CardClickableLink;
import io.bindernews.thegrackle.ui.MainMenuMetricsRequest;
import io.bindernews.thegrackle.variables.ExtraHitsVariable;
import io.bindernews.thegrackle.variables.Magic2Var;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


@SpireInitializer
public class GrackleMod implements
        AddAudioSubscriber, EditCharactersSubscriber, EditRelicsSubscriber, EditCardsSubscriber, EditStringsSubscriber,
        EditKeywordsSubscriber, PostInitializeSubscriber, PreUpdateSubscriber, OnStartBattleSubscriber
{
    public static final Logger log = LogManager.getLogger(GrackleMod.class);

    public static final String MOD_ID = "grackle";
    private static final String MOD_ID_COLON = MOD_ID + ":";
    public static final String MOD_RES = "grackleResources";

    /** Cache of loaded textures */
    private static final HashMap<String, Texture> textureCache = new HashMap<>();

    private static final Lazy<Map<String, String>> miscUI = Lazy.of(() ->
            CardCrawlGame.languagePack.getUIString(MOD_ID_COLON + "misc").TEXT_DICT);

    /**
     * Map of miscellaneous UI strings
     */
    public static Map<String, String> getMiscUI() {
        return miscUI.get();
    }

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


        /** The "Aloft" keyword */
        String KW_ALOFT = makeId("Aloft");
    }

    private static boolean hasInit = false;

    public GrackleMod() {
        BaseMod.subscribe(this);
        log.debug("registering color GRACKLE_BLACK");
        Grackle.Co.registerColor();
        log.debug(CO.REG_END, "colors");

        Events.getMetricsRun().on(metrics -> {
            if (metrics.type == Metrics.MetricRequestType.UPLOAD_METRICS && Grackle.isPlaying()) {
                ExtKt.sendPost(metrics, CO.METRICS_URL);
            }
        });
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        if (!hasInit) {
            GrackleMod mod = new GrackleMod();
            // Done
            hasInit = true;
        }
    }

    @Override
    public void receiveEditCharacters() {
        log.debug(CO.REG_START, "character");
        Grackle.register();
        log.debug(CO.REG_END, "character");
    }

    @Override
    public void receiveEditRelics() {
        log.debug(CO.REG_START, "relics");
        // Add shared relics
//        Stream.of(
//                new PhoenixIdol()
//        ).forEach(r -> BaseMod.addRelic(r, RelicType.SHARED));

        // Add class-specific relics
        Stream.of(
                new BerserkerTotem(),
                new LoftwingFeather(),
                new SimmeringHeat()
        ).forEach(r -> BaseMod.addRelicToCustomPool(r, Grackle.Co.COLOR_BLACK));
        log.debug(CO.REG_END, "relics");
    }

    @Override
    public void receiveEditCards() {
        IconsKt.registerIcons();
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
        Events.getPopups().on(CardClickableLink.getInst());
        Events.getPopups().on(MainMenuMetricsRequest.getInst());
    }

    @Override
    public void receivePreUpdate() {
        Events.getPopups().forEach(p -> {
            if (p.isEnabled()) p.update();
        });
    }

    private void registerPowers() {
        log.debug(CO.REG_START, "powers");
        AutoAdd aa = new AutoAdd(MOD_ID);
        aa.packageFilter(BasePower.class);
        for (val c : MiscUtil.autoFindClasses(aa, AbstractPower.class)) {
            BaseMod.addPower(c, MiscUtil.getPowerId(c));
        }
        log.debug(CO.REG_END, "powers");
    }

    private void registerDynamicVariables() {
        log.debug(CO.REG_START, "dynamic variables");
        BaseMod.addDynamicVariable(ExtraHitsVariable.inst);
        BaseMod.addDynamicVariable(Magic2Var.inst);
        log.debug(CO.REG_END, "dynamic variables");
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
        log.debug(CO.REG_START, "audio");
        val sfxPath = MOD_RES + "/audio/";
        BaseMod.addAudio(CO.SFX_QUACK, sfxPath + "duck_quack.ogg");
        log.debug(CO.REG_END, "audio");
    }


    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        ExtKt.setFireheartGained(AbstractDungeon.player, 0);
    }

    /**
     * Exists so that all cards are "used", and so we have an easily-sorted list of cards.
     */
    @SuppressWarnings("unused")
    private static AbstractCard[] makeCardList() {
        return new AbstractCard[] {
                new AAA(),
                new AerialAce(),
                new AerialAdvantage(),
                new AirToGroundMissiles(),
                new BePrepared(),
                new BombingRun(),
                new BufferInputs(),
                new BurnCream(),
                new Cackle(),
                new CopyCrow(),
                new CrashLanding(),
                new Death(),
                new Defend_GK(),
                new Duck(),
                new EagleEye(),
                new EmbodyFire(),
                new EvasiveManeuvers(),
                new FOOF(),
                new Forage(),
                new FightFire(),
                new FireTouch(),
                new FireWithin(),
                new FiredUpCard(),
                new Flock(),
                new GentleLanding(),
                new Grenenade(),
                new HangarMaintenance(),
                new HenPeck(),
                new InFlightService(),
                new MidairRefuel(),
                new Murder(),
                new Parachute(),
                new Paratrooper(),
                new PeckingOrder(),
                new PhoenixFeather(),
                new PhoenixForm(),
                new Plague(),
                new ResearchAndDev(),
                new RocketGrackle(),
                new Scratch(),
                new SelfBurn(),
                new SnapGracklePop(),
                new Strike_GK(),
                new SummonEgrets(),
                new Suplex(),
                new Swoop(),
                new Tailwind(),
                new Takeoff(),
                new TargetingComputer(),
                new ThisWillHurt(),
                new TryThatAgain(),
                new WildFire(),
                new WindowPain()
        };
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

    public static IMultiHitManager getMultiHitManager() {
        return ExtraHitsVariable.inst;
    }

    /**
     * Returns a new ID with the mod prefix.
     */
    public static @NotNull String makeId(String name) {
        return MOD_ID_COLON + name;
    }

    /**
     * Returns a new ID with the mod prefix and the class name as the suffix.
     */
    public static @NotNull String makeId(Class<?> clazz) {
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
