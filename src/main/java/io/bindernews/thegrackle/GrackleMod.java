package io.bindernews.thegrackle;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import io.bindernews.thegrackle.cards.BaseCard;
import io.bindernews.thegrackle.relics.PhoenixIdol;
import javassist.CtClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Stream;


@SpireInitializer
public class GrackleMod implements
        EditCharactersSubscriber, EditRelicsSubscriber, EditCardsSubscriber, EditStringsSubscriber,
        OnPowersModifiedSubscriber,
        OnStartBattleSubscriber
{
    public static final Logger log = LogManager.getLogger(GrackleMod.class);

    public static TextureAtlas cards;

    static final String ATTACK_DEFAULT_GRAY = Const.RES_IMAGES + "/512/bg_attack_default_gray.png";
    static final String SKILL_DEFAULT_GRAY = Const.RES_IMAGES + "/512/bg_skill_default_gray.png";
    static final String POWER_DEFAULT_GRAY = Const.RES_IMAGES + "/512/bg_power_default_gray.png";

    static final String ENERGY_ORB_DEFAULT_GRAY = Const.RES_IMAGES + "/512/card_default_gray_orb.png";
    static final String CARD_ENERGY_ORB = Const.RES_IMAGES + "/512/card_small_orb.png";

    static final String ATTACK_DEFAULT_GRAY_PORTRAIT = Const.RES_IMAGES + "/1024/bg_attack_default_gray.png";
    static final String SKILL_DEFAULT_GRAY_PORTRAIT = Const.RES_IMAGES + "/1024/bg_skill_default_gray.png";
    static final String POWER_DEFAULT_GRAY_PORTRAIT = Const.RES_IMAGES + "/1024/bg_power_default_gray.png";
    static final String RES_LANG = "grackleResources/localization";


    /**
     * Is the downfall mod installed?
     */
    public static boolean interopDownfall;

    private static boolean hasInit = false;

    public GrackleMod() {
        BaseMod.subscribe(this);

        interopDownfall = Loader.isModLoaded("downfall");

        Color C1 = Grackle.En.COLOR_MAIN;
        BaseMod.addColor(Grackle.En.COLOR_BLACK, C1, C1, C1, C1, C1, C1, C1,
                ATTACK_DEFAULT_GRAY, SKILL_DEFAULT_GRAY, POWER_DEFAULT_GRAY,
                ENERGY_ORB_DEFAULT_GRAY, CARD_ENERGY_ORB,
                ATTACK_DEFAULT_GRAY_PORTRAIT, SKILL_DEFAULT_GRAY_PORTRAIT, POWER_DEFAULT_GRAY_PORTRAIT);
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        if (!hasInit) {
            GrackleMod mod = new GrackleMod();
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
        Stream.of(
                new PhoenixIdol()
        ).forEach(r -> BaseMod.addRelic(r, RelicType.SHARED));

        // Add class-specific relics
//        Stream.of(
//                new ClassRelicA()
//        ).forEach(r -> BaseMod.addRelicToCustomPool(r, Grackle.En.COLOR_BLACK));
    }

    @Override
    public void receiveEditCards() {
        loadTextures();
        AutoAdd aa = new AutoAdd(Const.MOD_ID);
        aa.packageFilter(BaseCard.class);
        aa.cards();
//        Stream.of(
//                new Strike_GK(),
//                new Defend_GK(),
//                new Takeoff(),
//                new CrashLanding(),
//                new PhoenixForm(),
//                new Cackle(),
//                new PhoenixFeather(),
//                new HurricaneWind()
//        ).forEach(BaseMod::addCard);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void receivePowersModified() {
        AutoAdd aa = new AutoAdd(Const.MOD_ID);
        aa.packageFilter(GrackleMod.class);
        try {
            for (CtClass c : aa.findClasses(AbstractPower.class)) {
                Class<?> c2 = getClass().getClassLoader().loadClass(c.getName());
                MiscUtil.registerPower((Class<? extends AbstractPower>)c2);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
//        Stream.of(
//                CoolingPhoenixPower.class,
//                FireheartPower.class,
//                HealingPhoenixPower.class,
//                PhoenixFeatherPower.class,
//                PhoenixStancePower.class
//        ).forEach(GrackleMod::registerPower);
    }

    @Override
    public void receiveEditStrings() {
        MiscUtil.loadLocalization(RES_LANG, Settings.GameLanguage.ENG);
        if (Settings.language != Settings.GameLanguage.ENG) {
            MiscUtil.loadLocalization(RES_LANG, Settings.language);
        }
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {

    }

    public void loadTextures() {
        if (cards == null) {
            cards = new TextureAtlas(Gdx.files.classpath(Const.RES_IMAGES + "/cards/cards.atlas"));
            for (TextureAtlas.AtlasRegion r : cards.getRegions()) {
                CustomCard.imgMap.put(r.name, r.getTexture());
            }
        }
    }

    /**
     * Returns a new ID with the mod prefix.
     */
    public static String makeId(String name) {
        return Const.GK_ID + ":" + name;
    }

    /**
     * Returns a new ID with the mod prefix and the class name as the suffix.
     */
    public static String makeId(Class<?> clazz) {
        return makeId(clazz.getName());
    }

}
