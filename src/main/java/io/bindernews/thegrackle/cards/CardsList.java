package io.bindernews.thegrackle.cards;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Exists to remove all the "unused" warnings.
 */
@SuppressWarnings("unused")
public class CardsList {
    public static final ArrayList<BaseCard> cards = new ArrayList<>();
    static {
        Collections.addAll(cards,
                new AerialAce(),
                new AttackR(),
                new Cackle(),
                new CrashLanding(),
                new Defend_GK(),
                new FightFire(),
                new HurricaneWind(),
                new PhoenixFeather(),
                new PhoenixForm(),
                new TargetingComputer(),
                new PowerR(),
                new FireWithin(),
                new SelfBurn(),
                new SkillR(),
                new TryThatAgain(),
                new Strike_GK(),
                new Takeoff(),
                new TryThatAgain()
        );
    }
}
