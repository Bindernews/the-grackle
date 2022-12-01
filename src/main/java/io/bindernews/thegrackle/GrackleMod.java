package io.bindernews.thegrackle;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import io.bindernews.thegrackle.cards.*;
import io.bindernews.thegrackle.power.*;
import io.bindernews.thegrackle.relics.ClassRelicA;
import io.bindernews.thegrackle.relics.PhoenixIdol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Stream;

@SpireInitializer
public class GrackleMod implements
        EditCharactersSubscriber, EditRelicsSubscriber, EditCardsSubscriber, OnPowersModifiedSubscriber,
        OnStartBattleSubscriber
{
    public static final Logger log = LogManager.getLogger(GrackleMod.class);

    /**
     * Is the downfall mod installed?
     */
    public static boolean interopDownfall;

    /**
     * Fireheart gained in current fight
     */
    public static int fireheartGained = 0;

    public GrackleMod() {
        BaseMod.subscribe(this);
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        GrackleMod mod = new GrackleMod();
        interopDownfall = Loader.isModLoaded("downfall");
    }

    @Override
    public void receiveEditCharacters() {
        log.debug("registering characters");
        BaseMod.addCharacter(new Grackle(CardCrawlGame.playerName),
                Grackle.BUTTON, Grackle.PORTRAIT, Grackle.En.GRACKLE);
        log.debug("done registering characters");
    }

    @Override
    public void receiveEditRelics() {
        // Add shared relics
        Stream.of(
                new PhoenixIdol()
        ).forEach(r -> BaseMod.addRelic(r, RelicType.SHARED));

        // Add class-specific relics
        Stream.of(
                new ClassRelicA()
        ).forEach(r -> BaseMod.addRelicToCustomPool(r, Grackle.En.COLOR_BLACK));
    }

    @Override
    public void receiveEditCards() {
        Stream.of(
                new Strike_GK(),
                new Defend_GK(),
                new Takeoff(),
                new CrashLanding(),
                new PhoenixForm(),
                new Cackle(),
                new PhoenixFeather(),
                new HurricaneWind()
        ).forEach(BaseMod::addCard);
    }

    @Override
    public void receivePowersModified() {
        Stream.of(
                CoolingPhoenixPower.class,
                FireheartPower.class,
                HealingPhoenixPower.class,
                PhoenixFeatherPower.class,
                PhoenixStancePower.class
        ).forEach(GrackleMod::registerPower);
    }

    public static void registerPower(Class<? extends AbstractPower> clazz) {
        try {
            String powerId = (String) clazz.getDeclaredField("POWER_ID").get(null);
            BaseMod.addPower(clazz, powerId);
        } catch (SecurityException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {

    }

    /**
     * Returns a new ID with the mod prefix.
     */
    public static String makeId(String name) {
        return Const.GK_ID + ":" + name;
    }

    public static String makeId(Class<?> clazz) {
        return makeId(clazz.getName());
    }

}
