package io.bindernews.thegrackle.cards;

import io.bindernews.thegrackle.MiscUtil;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class CardsList {

    public static final ArrayList<BaseCard> cards = MiscUtil.arrayListOf(
            new AerialAce(),
            new AttackR(),
            new Cackle(),
            new CrashLanding(),
            new Defend_GK(),
            new FightFire(),
            new HurricaneWind(),
            new PhoenixFeather(),
            new PhoenixForm(),
            new PowerC(),
            new PowerR(),
            new PowerU(),
            new SkillC(),
            new SkillR(),
            new SkillU(),
            new Strike_GK(),
            new Takeoff()
    );
}
